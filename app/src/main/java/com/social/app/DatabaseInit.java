package com.social.app;

import com.social.app.model.Category;
import com.social.app.model.HintTag;
import com.social.app.repository.CategoryRepository;
import com.social.app.repository.HintTagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// include BEAN methods
public class DatabaseInit {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInit.class);
    @Bean
    CommandLineRunner initDB(HintTagRepository hintTagRepository, CategoryRepository categoryRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if(!hintTagRepository.findAll().isEmpty() || !categoryRepository.findAll().isEmpty()) return;

                Category vat_li = new Category("Vật lý", "Lĩnh vực nghiên cứu các hiện tượng vật lý trong tự nhiên, từ động lực học đến nhiệt độ và cường độ ánh sáng.");
                Category hoa_hoc = new Category("Hóa học", "Nghiên cứu về các phản ứng hóa học, cấu trúc của các hợp chất, và cách chúng tương tác để hiểu sâu hơn về thế giới xung quanh.");
                Category sinh_hoc = new Category( "Sinh học", "Khám phá và nghiên cứu về cuộc sống, từ cấu trúc tế bào đến sinh thái học và di truyền học.");
                Category toan_hoc = new Category( "Toán học", "Lĩnh vực nghiên cứu về số học, đại số, hình học, và lý thuyết số để giải quyết vấn đề và phát triển kiến thức toán học.");
                Category cong_nghe_thong_tin = new Category( "Công nghệ thông tin", "Thảo luận về các công nghệ, lập trình, và phát triển ứng dụng để nắm bắt cơ hội trong thế giới số hóa ngày nay.");
                Category ky_thuat = new Category("Kỹ thuật", "Lĩnh vực áp dụng khoa học và toán học để thiết kế và xây dựng các hệ thống và sản phẩm.");
                Category triet_hoc = new Category("Triết học", "Nghiên cứu về tư duy và tri thức, đặt ra câu hỏi về cuộc sống, tồn tại, và giá trị.");
                Category kinh_te = new Category( "Kinh tế học", "Nghiên cứu về tài chính, thị trường, và quản lý tài nguyên để hiểu cách kinh tế hoạt động.");
                Category xa_hoi = new Category( "Xã hội học", "Khám phá về hành vi con người, xã hội học, và vấn đề xã hội hiện tại.");
                Category lich_su = new Category( "Lịch sử", "Nghiên cứu về quá khứ, sự phát triển của văn hóa và xã hội qua thời gian.");
                Category chinh_tri = new Category( "Chính trị học", "Nghiên cứu về các hệ thống chính trị, quốc gia và quan hệ quốc tế.");
                Category tam_ly = new Category( "Tâm lý học", "Thảo luận về tư duy, cảm xúc, và hành vi con người để hiểu về tâm lý học.");
                Category luat = new Category( "Luật học", "Lĩnh vực nghiên cứu về quy định pháp luật, hệ thống pháp lý, và tư duy pháp lý.");
                Category giao_duc = new Category( "Giáo dục học", "Nghiên cứu về quá trình học và giảng dạy, tạo nền tảng cho sự phát triển của con người.");
                Category nghe_thuat = new Category( "Nghệ thuật", "Thảo luận về hình thức sáng tạo như hội họa, âm nhạc, diễn xuất và thể hiện tư duy và cảm xúc.");
                Category van_hoa = new Category( "Văn hóa", "Khám phá về truyền thống, giá trị và bản sắc văn hóa của một dân tộc hoặc cộng đồng.");
                Category y_hoc = new Category( "Y học", "Lĩnh vực nghiên cứu về sức khỏe, chẩn đoán và điều trị bệnh tật để cải thiện chất lượng cuộc sống.");
                Category the_thao = new Category( "Thể thao", "Bàn luận về các hoạt động thể thao, cách rèn luyện sức khỏe và đối thủ cạnh tranh trong các môn thể thao khác nhau.");
                // vat_li, hoa_hoc, sinh_hoc, toan_hoc, cong_nghe_thong_tin, ky_thuat, triet_hoc, kinh_te, xa_hoi, lich_su, chinh_tri, tam_ly, luat, giao_duc, nghe_thuat, van_hoa, y_hoc, the_thao
                categoryRepository.save(vat_li);
                categoryRepository.save(hoa_hoc);
                categoryRepository.save(sinh_hoc);
                categoryRepository.save(toan_hoc);
                categoryRepository.save(cong_nghe_thong_tin);
                categoryRepository.save(ky_thuat);
                categoryRepository.save(triet_hoc);
                categoryRepository.save(kinh_te);
                categoryRepository.save(xa_hoi);
                categoryRepository.save(lich_su);
                categoryRepository.save(chinh_tri);
                categoryRepository.save(tam_ly);
                categoryRepository.save(luat);
                categoryRepository.save(giao_duc);
                categoryRepository.save(nghe_thuat);
                categoryRepository.save(van_hoa);
                categoryRepository.save(y_hoc);
                categoryRepository.save(the_thao);

                hintTagRepository.save(new HintTag("ánh sáng", vat_li));
                hintTagRepository.save(new HintTag("cơ học", vat_li));
                hintTagRepository.save(new HintTag("điện", vat_li));
                hintTagRepository.save(new HintTag("sóng âm", vat_li));
                hintTagRepository.save(new HintTag("quang", vat_li));
                hintTagRepository.save(new HintTag("động học", vat_li));
                hintTagRepository.save(new HintTag("nhiệt độ", vat_li));
                hintTagRepository.save(new HintTag("áp suất", vat_li));
                hintTagRepository.save(new HintTag("khí quyển", vat_li));
                hintTagRepository.save(new HintTag("quán tính", vat_li));
                hintTagRepository.save(new HintTag("nhiệt độ", vat_li));

                hintTagRepository.save(new HintTag("hữu cơ", hoa_hoc));
                hintTagRepository.save(new HintTag("vô cơ", hoa_hoc));
                hintTagRepository.save(new HintTag("nguyên tử", hoa_hoc));
                hintTagRepository.save(new HintTag("phản ứng", hoa_hoc));
                hintTagRepository.save(new HintTag("hiện tượng", hoa_hoc));

                hintTagRepository.save(new HintTag("di truyền", sinh_hoc));
                hintTagRepository.save(new HintTag("tiến hóa", sinh_hoc));
                hintTagRepository.save(new HintTag("cơ thể người", sinh_hoc));
                hintTagRepository.save(new HintTag("vi sinh vật", sinh_hoc));
                hintTagRepository.save(new HintTag("vi-rút", sinh_hoc));

                hintTagRepository.save(new HintTag("đại số", toan_hoc));
                hintTagRepository.save(new HintTag("giải tích", toan_hoc));
                hintTagRepository.save(new HintTag("hình học", toan_hoc));
                hintTagRepository.save(new HintTag("phương trình", toan_hoc));
                hintTagRepository.save(new HintTag("xác suất", toan_hoc));
                hintTagRepository.save(new HintTag("hình học không gian", toan_hoc));

                hintTagRepository.save(new HintTag("mạng máy tính", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("hệ điều hành", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("an toàn thông tin", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("java", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("front end", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("back end", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("spring boot", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("devops", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("mobile app", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("machine learning", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("data science", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("cloud computing", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("cyber security", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("python", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("ai", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("kotlin", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("laravel", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("ruby", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("spring mvc", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("javascript", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("typescript", cong_nghe_thong_tin));

                hintTagRepository.save(new HintTag("tự động hóa", ky_thuat));
                hintTagRepository.save(new HintTag("xây dựng", ky_thuat));
                hintTagRepository.save(new HintTag("điện", ky_thuat));
                hintTagRepository.save(new HintTag("y tế", ky_thuat));
                hintTagRepository.save(new HintTag("viễn thông", ky_thuat));
                hintTagRepository.save(new HintTag("máy tính", ky_thuat));

                hintTagRepository.save(new HintTag("ngôn ngữ", triet_hoc));
                hintTagRepository.save(new HintTag("luân lý học", triet_hoc));
                hintTagRepository.save(new HintTag("nhân đạo học", triet_hoc));
                hintTagRepository.save(new HintTag("triết học chính trị", triet_hoc));
                hintTagRepository.save(new HintTag("đạo học", triet_hoc));
                hintTagRepository.save(new HintTag("phê phán xã hội", triet_hoc));

                hintTagRepository.save(new HintTag("tài chính", kinh_te));
                hintTagRepository.save(new HintTag("marketing", kinh_te));
                hintTagRepository.save(new HintTag("logistic", kinh_te));
                hintTagRepository.save(new HintTag("kinh doanh quốc tế", kinh_te));
                hintTagRepository.save(new HintTag("quản trị kinh doanh", kinh_te));
                hintTagRepository.save(new HintTag("tiền tệ", kinh_te));

                hintTagRepository.save(new HintTag("gia đình", xa_hoi));
                hintTagRepository.save(new HintTag("nhân quyền", xa_hoi));
                hintTagRepository.save(new HintTag("luật pháp", xa_hoi));
                hintTagRepository.save(new HintTag("hôn nhân", xa_hoi));
                hintTagRepository.save(new HintTag("phụ nữ", xa_hoi));

                hintTagRepository.save(new HintTag("lịch sử đại cương", lich_su));
                hintTagRepository.save(new HintTag("vua chúa", lich_su));
                hintTagRepository.save(new HintTag("Việt Nam", lich_su));
                hintTagRepository.save(new HintTag("nhân loại", lich_su));
                hintTagRepository.save(new HintTag("thế giới", lich_su));
                hintTagRepository.save(new HintTag("chính trị", lich_su));

                hintTagRepository.save(new HintTag("Việt Nam", chinh_tri));
                hintTagRepository.save(new HintTag("Mỹ", chinh_tri));
                hintTagRepository.save(new HintTag("Nga", chinh_tri));
                hintTagRepository.save(new HintTag("thế giới", chinh_tri));
                hintTagRepository.save(new HintTag("pháp luật", chinh_tri));
                hintTagRepository.save(new HintTag("xã hội", chinh_tri));

                hintTagRepository.save(new HintTag("tâm lý tội phạm", tam_ly));
                hintTagRepository.save(new HintTag("gia đình", tam_ly));
                hintTagRepository.save(new HintTag("tâm lý xã hội học", tam_ly));
                hintTagRepository.save(new HintTag("tâm lý nhân cách", tam_ly));
                hintTagRepository.save(new HintTag("tâm lý học", tam_ly));

                hintTagRepository.save(new HintTag("hành chính", luat));
                hintTagRepository.save(new HintTag("hình sự", luat));
                hintTagRepository.save(new HintTag("tư pháp", luat));
                hintTagRepository.save(new HintTag("hôn nhân gia đình", luat));
                hintTagRepository.save(new HintTag("dân sự", luat));
                hintTagRepository.save(new HintTag("luật sư", luat));

                hintTagRepository.save(new HintTag("trẻ em chậm phát triển", giao_duc));
                hintTagRepository.save(new HintTag("trẻ em", giao_duc));
                hintTagRepository.save(new HintTag("Việt Nam", giao_duc));
                hintTagRepository.save(new HintTag("đại học", giao_duc));
                hintTagRepository.save(new HintTag("tiểu học", giao_duc));
                hintTagRepository.save(new HintTag("sư phạm", giao_duc));

                hintTagRepository.save(new HintTag("cải lương", nghe_thuat));
                hintTagRepository.save(new HintTag("hội họa", nghe_thuat));
                hintTagRepository.save(new HintTag("cải lương", nghe_thuat));
                hintTagRepository.save(new HintTag("sân khấu", nghe_thuat));
                hintTagRepository.save(new HintTag("biểu diễn", nghe_thuat));
                hintTagRepository.save(new HintTag("âm nhạc",nghe_thuat));
                hintTagRepository.save(new HintTag("âm nhạc cổ điển", nghe_thuat));
                hintTagRepository.save(new HintTag("nhà hát", nghe_thuat));
                hintTagRepository.save(new HintTag("điện ảnh", nghe_thuat));

                hintTagRepository.save(new HintTag("Bắc Trung Nam", van_hoa));
                hintTagRepository.save(new HintTag("Mỹ Latinh", van_hoa));
                hintTagRepository.save(new HintTag("Châu Âu", van_hoa));
                hintTagRepository.save(new HintTag("Châu Phi", van_hoa));
                hintTagRepository.save(new HintTag("Châu Á", van_hoa));
                hintTagRepository.save(new HintTag("Việt Nam", van_hoa));
                hintTagRepository.save(new HintTag("nghệ thuật", van_hoa));

                hintTagRepository.save(new HintTag("bác sĩ", y_hoc));
                hintTagRepository.save(new HintTag("sức khỏe", y_hoc));
                hintTagRepository.save(new HintTag("đa khoa", y_hoc));
                hintTagRepository.save(new HintTag("răng hàm mặt", y_hoc));
                hintTagRepository.save(new HintTag("thẩm mỹ", y_hoc));
                hintTagRepository.save(new HintTag("x-quang", y_hoc));

                hintTagRepository.save(new HintTag("bóng đá", the_thao));
                hintTagRepository.save(new HintTag("bóng rổ", the_thao));
                hintTagRepository.save(new HintTag("tennis", the_thao));
                hintTagRepository.save(new HintTag("bóng bầu dục", the_thao));
                hintTagRepository.save(new HintTag("võ thuật", the_thao));
                hintTagRepository.save(new HintTag("điền kinh", the_thao));
                hintTagRepository.save(new HintTag("bơi lội", the_thao));
                hintTagRepository.save(new HintTag("bóng bàn", the_thao));
                hintTagRepository.save(new HintTag("quyền anh", the_thao));
                hintTagRepository.save(new HintTag("gôn", the_thao));
                hintTagRepository.save(new HintTag("đua ngựa", the_thao));

            }
        };
    }
}
