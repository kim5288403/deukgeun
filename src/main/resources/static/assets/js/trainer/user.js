	//트레이너 정보 셋팅
	function setUserDetail(data) {
				$.each(data, function(index, value) {
					if(index === "html") {
						$("#" + index).append(value);

					} else if(index === "profile") {
						$("#profile").attr("src", value.path);

					} else if (index === "price") {
						$("label[for=price]").text("PT횟수 1회 당 ( " + value + "원 )");

					} else if (index === "gender") {
						$("#" + index).text("성별 : " + value);

					} else if (index === "group") {
						$("span[name=groupName]").text(value.groupName + " ");

					} else if (index === "address") {
					    let address = value.roadAddress
					     + " " + value.extraAddress
					     + " " + value.postcode;
                     	$("input[name=address]").val(address);

                    } else if (index === "licenses") {
                        setUserLicense(value);

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
	
		