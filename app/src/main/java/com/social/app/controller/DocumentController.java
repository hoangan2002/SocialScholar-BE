package com.social.app.controller;

import com.social.app.entity.DocumentResponse;
import com.social.app.entity.ResponseObject;
import com.social.app.model.Document;
import com.social.app.model.User;
import com.social.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/doc")
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @Autowired
    ResponseConvertService responseConvertService;

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
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        if(!userService.isGroupMember(userid, groupid))
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
    @GetMapping("/documents")
    public ArrayList<DocumentResponse> retrieveAllApprovedDocument(){
        ArrayList<Document> result = documentService.allApprovedDocuments();
        return responseConvertService.documentResponseArrayList(result);
    }
    @GetMapping("/documents/waiting")
    public ArrayList<DocumentResponse> retrieveAllUnApprovedDocument(){
        ArrayList<Document> result = documentService.allUnApprovedDocuments();
        return responseConvertService.documentResponseArrayList(result);
    }
    @GetMapping("/my-documents")
    public ArrayList<DocumentResponse> UserApporevedCreatedDocuments(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        ArrayList<Document> result = documentService.UserApprovedCreatedDocuments(user);
        return responseConvertService.documentResponseArrayList(result);
    }
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
    public ResponseEntity<?> getAvatarUri(@PathVariable("docId") long docId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = userService.findUserByUsername(authentication.getName());
        Document documentDB = documentService.findDocumentbyId(docId);
        if (documentDB==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("The document is not exist", "failed", ""));
        if(documentDB.getAuthor().getUserId()!=theUser.getUserId() || billService.findByDocumentAndUser(documentDB,theUser)!=null)
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
}
