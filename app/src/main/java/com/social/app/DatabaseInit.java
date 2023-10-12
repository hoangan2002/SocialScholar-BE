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

                hintTagRepository.save(new HintTag("anh_sang", vat_li));
                hintTagRepository.save(new HintTag("co_hoc", vat_li));
                hintTagRepository.save(new HintTag("dien", vat_li));
                hintTagRepository.save(new HintTag("song_am", vat_li));
                hintTagRepository.save(new HintTag("quang", vat_li));
                hintTagRepository.save(new HintTag("dong_hoc", vat_li));
                hintTagRepository.save(new HintTag("nam_cham", vat_li));
                hintTagRepository.save(new HintTag("ap_suat", vat_li));
                hintTagRepository.save(new HintTag("khi_quyen", vat_li));
                hintTagRepository.save(new HintTag("quan_tinh", vat_li));
                hintTagRepository.save(new HintTag("nhiet_do", vat_li));

                hintTagRepository.save(new HintTag("huu_co", hoa_hoc));
                hintTagRepository.save(new HintTag("vo_co", hoa_hoc));
                hintTagRepository.save(new HintTag("nguyen_tu", hoa_hoc));
                hintTagRepository.save(new HintTag("phan_ung", hoa_hoc));
                hintTagRepository.save(new HintTag("hien_tuong", hoa_hoc));

                hintTagRepository.save(new HintTag("di_truyen", sinh_hoc));
                hintTagRepository.save(new HintTag("tien_hoa", sinh_hoc));
                hintTagRepository.save(new HintTag("co_the_nguoi", sinh_hoc));
                hintTagRepository.save(new HintTag("vi_sinh_vat", sinh_hoc));
                hintTagRepository.save(new HintTag("virus", sinh_hoc));

                hintTagRepository.save(new HintTag("dai_so", toan_hoc));
                hintTagRepository.save(new HintTag("giai_tich", toan_hoc));
                hintTagRepository.save(new HintTag("hinh_hoc", toan_hoc));
                hintTagRepository.save(new HintTag("phuong_trinh", toan_hoc));
                hintTagRepository.save(new HintTag("xac_suat", toan_hoc));
                hintTagRepository.save(new HintTag("hinh_hoc_khong_gian", toan_hoc));

                hintTagRepository.save(new HintTag("mang_may_tinh", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("he_dieu_hanh", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("an_toan_thong_tin", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("java", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("front_end", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("back_end", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("spring_boot", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("devops", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("mobile_app", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("machine_learning", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("data_science", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("cloud_computing", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("cyber_security", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("python", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("ai", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("kotlin", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("laravel", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("ruby", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("spring_mvc", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("javascript", cong_nghe_thong_tin));
                hintTagRepository.save(new HintTag("typescript", cong_nghe_thong_tin));

                hintTagRepository.save(new HintTag("tu_dong_hoa", ky_thuat));
                hintTagRepository.save(new HintTag("xay_dung", ky_thuat));
                hintTagRepository.save(new HintTag("dien", ky_thuat));
                hintTagRepository.save(new HintTag("y_te", ky_thuat));
                hintTagRepository.save(new HintTag("vien_thong", ky_thuat));
                hintTagRepository.save(new HintTag("may_tinh", ky_thuat));

                hintTagRepository.save(new HintTag("ngon_ngu", triet_hoc));
                hintTagRepository.save(new HintTag("luan_ly_hoc", triet_hoc));
                hintTagRepository.save(new HintTag("nhan_dao_hoc", triet_hoc));
                hintTagRepository.save(new HintTag("triet_hoc_chinh_tri", triet_hoc));
                hintTagRepository.save(new HintTag("dao_hoc", triet_hoc));
                hintTagRepository.save(new HintTag("phe_phan_xa_hoi", triet_hoc));

                hintTagRepository.save(new HintTag("tai_chinh", kinh_te));
                hintTagRepository.save(new HintTag("marketing", kinh_te));
                hintTagRepository.save(new HintTag("logistic", kinh_te));
                hintTagRepository.save(new HintTag("kinh_doanh_quoc_te", kinh_te));
                hintTagRepository.save(new HintTag("quan_tri_kinh_doanh", kinh_te));
                hintTagRepository.save(new HintTag("tien_te", kinh_te));

                hintTagRepository.save(new HintTag("gia_dinh", xa_hoi));
                hintTagRepository.save(new HintTag("nhan_quyen", xa_hoi));
                hintTagRepository.save(new HintTag("luat_phap", xa_hoi));
                hintTagRepository.save(new HintTag("hon_nhan", xa_hoi));
                hintTagRepository.save(new HintTag("phu_nu", xa_hoi));

                hintTagRepository.save(new HintTag("lich_su_dai_cuong", lich_su));
                hintTagRepository.save(new HintTag("vua_chua", lich_su));
                hintTagRepository.save(new HintTag("Viet_Nam", lich_su));
                hintTagRepository.save(new HintTag("nhan_loai ", lich_su));
                hintTagRepository.save(new HintTag("the_gioi ", lich_su));
                hintTagRepository.save(new HintTag("chinh_tri ", lich_su));

                hintTagRepository.save(new HintTag("Viet_Nam", chinh_tri));
                hintTagRepository.save(new HintTag("My", chinh_tri));
                hintTagRepository.save(new HintTag("Nga", chinh_tri));
                hintTagRepository.save(new HintTag("the_gioi", chinh_tri));
                hintTagRepository.save(new HintTag("phap_luat", chinh_tri));
                hintTagRepository.save(new HintTag("xa_hoi ", chinh_tri));

                hintTagRepository.save(new HintTag("tam_ly_toi_pham", tam_ly));
                hintTagRepository.save(new HintTag("gia_dinh", tam_ly));
                hintTagRepository.save(new HintTag("tam_ly_xa_hoi_hoc", tam_ly));
                hintTagRepository.save(new HintTag("tam_ly_nhan_cach ", tam_ly));
                hintTagRepository.save(new HintTag("tam_ly_hoc ", tam_ly));

                hintTagRepository.save(new HintTag("hanh_chinh", luat));
                hintTagRepository.save(new HintTag("hinh_su", luat));
                hintTagRepository.save(new HintTag("tu_phap", luat));
                hintTagRepository.save(new HintTag("hon_nhan_gia_dinh", luat));
                hintTagRepository.save(new HintTag("dan_su", luat));
                hintTagRepository.save(new HintTag("luat_su ", luat));

                hintTagRepository.save(new HintTag("tre_em_cham_phat_trien", giao_duc));
                hintTagRepository.save(new HintTag("tre_em", giao_duc));
                hintTagRepository.save(new HintTag("Viet_Nam", giao_duc));
                hintTagRepository.save(new HintTag("dai_hoc", giao_duc));
                hintTagRepository.save(new HintTag("tieu_hoc", giao_duc));
                hintTagRepository.save(new HintTag("su_pham", giao_duc));

                hintTagRepository.save(new HintTag("cai_luong", nghe_thuat));
                hintTagRepository.save(new HintTag("hoi_hoa", nghe_thuat));
                hintTagRepository.save(new HintTag("ca_tru", nghe_thuat));
                hintTagRepository.save(new HintTag("san_khau", nghe_thuat));
                hintTagRepository.save(new HintTag("bieu_dien", nghe_thuat));
                hintTagRepository.save(new HintTag("am_nhac", nghe_thuat));
                hintTagRepository.save(new HintTag("am_nhac_co_dien", nghe_thuat));
                hintTagRepository.save(new HintTag("nha_hat", nghe_thuat));
                hintTagRepository.save(new HintTag("dien_anh", nghe_thuat));


                hintTagRepository.save(new HintTag("bac_trung_nam", van_hoa));
                hintTagRepository.save(new HintTag("My_Latin", van_hoa));
                hintTagRepository.save(new HintTag("chau_Au", van_hoa));
                hintTagRepository.save(new HintTag("chau_Phi", van_hoa));
                hintTagRepository.save(new HintTag("chau_A", van_hoa));
                hintTagRepository.save(new HintTag("Viet_Nam", van_hoa));
                hintTagRepository.save(new HintTag("nghe_thuat", van_hoa));

                hintTagRepository.save(new HintTag("bac_si", y_hoc));
                hintTagRepository.save(new HintTag("suc_khoe", y_hoc));
                hintTagRepository.save(new HintTag("da_khoa", y_hoc));
                hintTagRepository.save(new HintTag("rang_ham_mat", y_hoc));
                hintTagRepository.save(new HintTag("tham_my", y_hoc));
                hintTagRepository.save(new HintTag("x_quang", y_hoc));

                hintTagRepository.save(new HintTag("bong_da", the_thao));
                hintTagRepository.save(new HintTag("bong_ro", the_thao));
                hintTagRepository.save(new HintTag("tennis", the_thao));
                hintTagRepository.save(new HintTag("bong_bau_duc", the_thao));
                hintTagRepository.save(new HintTag("vo_thuat", the_thao));
                hintTagRepository.save(new HintTag("dien_kinh", the_thao));
                hintTagRepository.save(new HintTag("boi_loi", the_thao));
                hintTagRepository.save(new HintTag("bong_ban", the_thao));
                hintTagRepository.save(new HintTag("quyen_anh", the_thao));
                hintTagRepository.save(new HintTag("golf", the_thao));
                hintTagRepository.save(new HintTag("dua_ngua", the_thao));

            }
        };
    }
}
