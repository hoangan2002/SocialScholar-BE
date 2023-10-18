package com.social.app.controller;

import com.itextpdf.commons.utils.Base64;
import com.social.app.dto.DocumentDTO;
import com.social.app.entity.DocumentResponse;
import com.social.app.entity.ResponseObject;
import com.social.app.model.Document;
import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/doc")
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @Autowired
    UserService userService;
    @Autowired
    GroupServices groupServices;
    @Autowired
    ImageStorageService storageService;
    @Autowired
    BillService billService;

    @PostMapping("/document")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createDocument(@RequestPart Document document,
                                                      @RequestParam("file")MultipartFile file,
                                                      @RequestParam("groupid") int groupid)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        int userid = user.getUserId();
        if(!userService.isGroupMember(user.getUserName(), groupid))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("User must be in group","Failed","")
            );
        try {
            if(userService.loadUserById(userid)!=null){
                document.setGroup(groupServices.loadGroupById(groupid));
                document.setAuthor(userService.loadUserById(userid));
                String url = storageService.storeDoc(file);
                document.setUrl(url);
                // chua duyet document
                document.setApproved(false);
                documentService.saveNew(document);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Create successfully", document.getDocumentName()));
            }

        }catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("There are problem..", "failed", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Create Error", "Failed", ""));
    }
    // -------------------------------------------- DOCUMENT LIST ------------------------------------------------------
    //                                                                                                                --
    // Tat ca doc đã duyệt
    @GetMapping("/documents")
    public ArrayList<DocumentDTO> retrieveAllApprovedDocument(){
        ArrayList<Document> result = documentService.allApprovedDocuments();
        return documentService.ListDocumentDTO(result);
    }
    //                                                                                                                --
    // Tat ca doc cho duyệt
    @GetMapping("/documents/waiting")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    public ArrayList<DocumentDTO> retrieveAllUnApprovedDocument(){
        ArrayList<Document> result = documentService.allUnApprovedDocuments();
        return documentService.ListDocumentDTO(result);
    }
    //                                                                                                                --
    // Tat ca doc dc duyet cua user
    @GetMapping("/my-documents")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ArrayList<DocumentDTO> UserApprovedCreatedDocuments(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        ArrayList<Document> result = documentService.UserApprovedCreatedDocuments(user);
        return documentService.ListDocumentDTO(result);
    }
    // Tat ca doc user da mua
    @GetMapping("/bought-documents")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ArrayList<DocumentDTO> BoughtDocuments(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        ArrayList<Document> result = documentService.BoughtDocuments(user);
        return documentService.ListDocumentDTO(result);
    }

    // Tat ca doc duoc duyet cua group
    @GetMapping("group-documents/{groupId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ArrayList<DocumentDTO> GroupApprovedDocuments(@PathVariable("groupId") long id){
        Groups groups = groupServices.loadGroupById(id);
        if (groups==null) throw new RuntimeException("Group is not exist");
        ArrayList<Document> result = documentService.GroupApprovedDocuments(groups);
        return documentService.ListDocumentDTO(result);
    }
    //                                                                                                                --
    // -----------------------------------------------------------------------------------------------------------------

    @DeleteMapping("/document/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public  ResponseEntity<ResponseObject> userDeleteDocument(@PathVariable("docId") long docId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        try{
            Document document = documentService.findDocumentbyId(docId);
            if (document==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("The document is not exist", "failed", ""));
            if(document.getAuthor().getUserId()!=userid)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("The user don't own this document", "failed", ""));
            // xoa file trong thu muc
            String deletePath = storageService.getUploadsPath()+document.getUrl();
            File deleteFile = new File(deletePath);
            if(deleteFile.exists())
                storageService.deleteFile(deletePath);
            // xoa document
            String result = documentService.deleteDocumentById(docId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete successfully", result));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(e.getMessage(), "failed", ""));
        }
    }
    @PutMapping("/my-documents/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public  ResponseEntity<ResponseObject> userEditDocument(@PathVariable("docId") long docId, @RequestPart Document documentData){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        try{
            Document documentDB = documentService.findDocumentbyId(docId);
            if (documentDB==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("The document is not exist", "failed", ""));
            if(documentDB.getAuthor().getUserId()!=userid)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("The user don't own this document", "failed", ""));
            documentDB.setDocumentName(documentData.getDocumentName());
            documentDB.setDescription(documentData.getDescription());
            documentDB.setCost(documentData.getCost());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Edit successfully"
                            , documentService.update(documentDB).getDocumentName()));

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(e.getMessage(), "failed", ""));
        }
    }


    @GetMapping("/download/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ResponseEntity<?> downloadDocument(@PathVariable("docId") long docId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = userService.findUserByUsername(authentication.getName());
        Document documentDB = documentService.findDocumentbyId(docId);
        if (documentDB==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("The document is not exist", "failed", ""));
        if(documentDB.getAuthor().getUserId()!=theUser.getUserId() && billService.findByDocumentAndUser(documentDB,theUser)==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("The user does not have download permission", "failed", ""));
        Resource resource = null;
        String filename = documentDB.getUrl();
        File file = new File(storageService.getUploadsPath()+filename);
        resource = storageService.loadAsResource(file);

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body( resource);
    }

    @PostMapping("/buy/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public  ResponseEntity<ResponseObject> buyDocument(@PathVariable("docId") long docId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customer = userService.findUserByUsername(authentication.getName());
        Document document = documentService.findDocumentbyId(docId);
        if (document==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("The document is not exist", "failed", ""));
        // Neu da mua roi thi not dc mua nua
        if (billService.findByDocumentAndUser(document,customer)!=null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("User has bought document", "failed", ""));
        // Neu la author thi not can mua
        if(document.getAuthor().getUserId()==customer.getUserId())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("User is author", "failed", ""));
        try {
            // check coins cua customer vs cost cua document
            if (customer.getCoin() < document.getCost())
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("Not enough Coins", "failed", ""));
            boolean result = documentService.DocumentExchangeTransaction(customer,document);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Transaction successfully"
                            , result));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Transaction failed", "failed", ""));
        }

    }

    // Admin duyet doc oke
    @PutMapping("/approve/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    public  ResponseEntity<ResponseObject> ApproveDocument(@PathVariable("docId") long docId){
        try{
            Document document = documentService.findDocumentbyId(docId);
            if (document==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("The document is not exist", "failed", ""));
            if (document.isApproved())
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("The document has already been approved", "failed", ""));
            document.setApproved(true);
            documentService.update(document);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update successfully", "Approved"));
        } catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject(exception.getMessage(), "failed", ""));
        }

    }

    // Admin xoa doc
    @DeleteMapping ("/delete/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    public  ResponseEntity<ResponseObject> AdminDeleteDocument(@PathVariable("docId") long docId){
        try {
            Document document = documentService.findDocumentbyId(docId);
            if (document==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("The document is not exist", "failed", ""));
            if (document.isApproved())
                System.out.println("Co le tai lieu da vi pham mot so tieu chuan, nen bi xoa");
            else System.out.println("Tai lieu cua ban duoc chap nhan phe duyet");

            // xoa file trong thu muc
            String deletePath = storageService.getUploadsPath()+document.getUrl();
            File deleteFile = new File(deletePath);
            if(deleteFile.exists())
                storageService.deleteFile(deletePath);

            // xoa document
            String result = documentService.deleteDocumentById(docId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete successfully", result));
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject(exception.getMessage(), "failed", ""));
        }
    }
    @GetMapping("/preview/{docId}")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ResponseEntity<?> Test(@PathVariable("docId") long docId) throws IOException {
        Document documentDB = documentService.findDocumentbyId(docId);
        if (documentDB==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("The document is not exist", "failed", ""));

        String filename = documentDB.getUrl();

        ByteArrayInputStream bis = storageService.PreviewDocument(filename);

        if (bis == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/pdf";
        String headerValue = "inline; filename=migration.pdf";

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body( new InputStreamResource(bis));
    }
}
