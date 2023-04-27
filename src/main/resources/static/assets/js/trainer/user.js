	//트레이너 정보 셋팅
	function setUserInfo(data) {
				$.each(data, function(index, value) {
					if(index === "html") {
						$("#" + index).append(value);
					} else if(index === "path") {
						$("#profile").attr("src",  "/images/trainer/profile/" + value);
					} else if (index === "price") {
						$("label[id=price]").text("PT횟수 1회 당 ( " + data.price + "원 )");
					} else if (index === "gender") {
						$("#" + index).text("성별 : " + value);
					} else if (index === "groupName") {
						$("#" + index).text(value + " 주소");
					} else {
						$("input[name=" + index + "]").val(value);
					}
				});
	}
	
	//트레이너 자격증 정보 셋팅		
	function setUserLicense(data) {
		let appendHtml = "";
        if (data?.length) {
            $.each(data, function(index, value) {
                appendHtml += "<li>" + value.certificateName + "</li>";
            });
        } else {
            appendHtml = "<li>등록한 자격증이 존재하지 않습니다.</li>";
        }
        $("ul[id=license]").append(appendHtml);
	}
	
		