package com.example.deukgeun.trainer.response;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class MemberResponse {
    private String email;

    private String name;

    private GroupStatus groupStatus;

    private Gender gender;

    private String groupName;

    private String postcode;

    private String jibunAddress;

    private String roadAddress;

    private String detailAddress;

    private String extraAddress;

    private String introduction;

    private Integer price;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.groupStatus = member.getGroupStatus();
        this.groupName = member.getGroupName();
        this.postcode = member.getPostcode();
        this.jibunAddress = member.getJibunAddress();
        this.roadAddress = member.getRoadAddress();
        this.detailAddress = member.getDetailAddress();
        this.extraAddress = member.getExtraAddress();
        this.gender = member.getGender();
        this.price = member.getPrice();
        this.introduction = member.getIntroduction();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberListResponse {

        private Long id;

        private String name;

        private String path;

        private GroupStatus groupStatus;

        private String groupName;

        private String introduction;

        public MemberListResponse(Profile profile) {
            this.id = profile.getMemberId();
            this.path = profile.getPath();
            this.name = profile.getMember().getName();
            this.groupStatus = profile.getMember().getGroupStatus();
            this.groupName = profile.getMember().getGroupName();
            this.introduction = profile.getMember().getIntroduction();

        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserListPaginationResponse {

        private List<MemberListResponse> list;

        private Integer totalPages;

        private Integer currentPage;

        public UserListPaginationResponse(Page<MemberListResponse> page, Integer currentPage) {
            this.list = page.getContent();
            this.totalPages = page.getTotalPages();
            this.currentPage = currentPage;
        }
    }

}
