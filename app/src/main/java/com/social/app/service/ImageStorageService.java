package com.social.app.service;

import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.social.app.repository.PostRepository;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.itextpdf.layout.properties.TextAlignment.CENTER;
import static com.itextpdf.layout.properties.VerticalAlignment.TOP;
import static java.lang.Math.PI;

@Service
public class ImageStorageService implements IStorageService{
    @Getter
    private final Path storageFolder = Paths.get("uploads");
    //constructor

    @Autowired
    PostRepository postRepository;
    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);
        }catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }
    private boolean isImageFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
// kiểm tra có phải là document hay không.
    private boolean isDocumentFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"pdf","docx"})
                .contains(fileExtension.trim().toLowerCase());
    }
    private boolean isDocx(String path) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(path);
        return Arrays.asList(new String[] {"docx"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("haha");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            //check file is image ?
            if(!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            //file must be <= 5Mb
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if(fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb");
            }
            //File must be rename, why ?
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                            Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to store file.", exception);
        }
    }
    public String storeDoc(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            //check file is Doc ?
            if(!isDocumentFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            //file must be <= 5Mb
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if(fileSizeInMegabytes > 100.0f) {
                throw new RuntimeException("File must be <= 100Mb");
            }
            //File must be rename, why ?
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                            Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            //list all files in storageFolder
            //How to fix this ?
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }

    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }

    public boolean deleteFile(String path){
        File file = new File(path);
        System.out.println(getUploadsPath());
        if(file.exists())
            return file.delete();
        return false;
    }

    public String getUploadsPath(){
        return String.valueOf(Paths.get("uploads").toAbsolutePath()+File.separator);
    }

    public String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeBase64URLSafeString(bytes).toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }

    public Resource loadAsResource(File file){
        try{

            Path path = Path.of(file.getAbsolutePath());
            System.out.println(path+"              AAAAAAAAAAAAAAAAAAAAAA");
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists()|| resource.isReadable()){
                return resource;
            }
            throw  new RuntimeException("Can not read file: "+ file);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public ByteArrayInputStream PreviewDocument(String path) throws IOException {

        if(isDocx(path)){
            String pdfName = path.replace(".docx",".pdf");
            File docxFile = new File(path);
            File pdfFile = new File(pdfName);

            if(!pdfFile.exists()){
                try(InputStream inputStream = new FileInputStream(getUploadsPath()+path);
                    OutputStream outputStream = new FileOutputStream(getUploadsPath()+pdfName)) {
                    XWPFDocument document = new XWPFDocument(inputStream);
                    PdfOptions options = PdfOptions.create();
                    // Convert .docx file to .pdf file
                    PdfConverter.getInstance().convert(document, outputStream, options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            path = pdfName;
        }

        // Rut gon doc
        PdfReader reader = new PdfReader(getUploadsPath()+path);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument srcDocument = new PdfDocument(reader);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        PdfPageFormCopier formCopier = new PdfPageFormCopier();
        int pages = srcDocument.getNumberOfPages();

        IPdfPageExtraCopier copier = new PdfPageFormCopier();
        if (pages>12){
            srcDocument.copyPagesTo(1,12,pdfDocument,copier);

        }
        else
            srcDocument.copyPagesTo(1,pages,pdfDocument,copier);


        // Tao chu watermark
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        Text text = new Text("Schoolar School");
        text.setFont(font);
        text.setFontSize(100);
        text.setOpacity(0.7f);
        Paragraph paragraph = new Paragraph(text);
        // Tao do nghieng va opacity
        PdfPage pdfPage = document.getPdfDocument().getPage(1);
        PageSize pageSize = (PageSize) pdfPage.getPageSizeWithRotation();
        float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
        float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
        float xOffset = 100f / 2;
        float verticalOffset = 100f / 4;
        float rotationInRadians = (float) (PI / 180 * 60f);

        // Add watermark
        for(int i=1; i<= pdfDocument.getNumberOfPages();i++) {
            document.showTextAligned(paragraph, x - xOffset, y + verticalOffset,
                    i, CENTER, TOP, rotationInRadians);
        }

        // Add img
        ImageData imageData = ImageDataFactory.create(getUploadsPath()+"Logo.jpg");
        Image image = new Image(imageData);
        image.setFixedPosition(7,0);
        image.setOpacity(0.9f);
//        image.setWidth(pdfDocument.getDefaultPageSize().getWidth());
//        image.setHeight(pdfDocument.getDefaultPageSize().getHeight());
        image.scaleAbsolute(pdfDocument.getDefaultPageSize().getWidth(),pdfDocument.getDefaultPageSize().getHeight());
        for(int i=2; i<= pdfDocument.getNumberOfPages();i++){
            PdfPage page = document.getPdfDocument().getPage(i);
            PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamAfter(),
                    page.getResources(), pdfDocument);
            Rectangle area = page.getPageSize();
            new Canvas(aboveCanvas, area)
                    .add(image);
        }
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
