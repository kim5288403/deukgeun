function isEmpty(str){
		if(typeof str == "undefined" || str == null || str == "")
			return true;
		else
			return false ;
}

function defaultSuccessAlert(text, title) {
	return Swal.fire({
		title : title ? title : "success",
		text : text,
		icon : "success",
		confirmButtonText: "확인"
		});
}

function defaultQuestionAlert(text, title) {
	return Swal.fire({
		title : title ? title : "success",
		text : text,
		icon : "question",
		showCancelButton: true,
		confirmButtonText: "확인",
		cancelButtonText: "취소"
		});
}

function defaultErrorAlert(text, title) {
	return Swal.fire({
		title : title ? title : "Error",
		text : text,
		icon : "warning",
		confirmButtonText: "확인"
		})
}

function errorMessage(data) {
	errorMessageReset();
	$.each(data, function (index, item) {
		if (index === "valid_AuthMailCode" || index === "valid_AuthEmail") {
			$("span[class=errorMessage][id=valid_code]").text(item);
		} else {
			$("span[class=errorMessage][id='" + index + "']").text(item);
		}
	});
}
		
function errorMessageReset() {
	$("span[class=errorMessage]").text("");
}

function setCookie(key, value) {
	//const d = new Date();
 	//d.setTime(d.getTime() + (1*60*1000));
  	//let expires = "expires="+ d.toUTCString();
	//document.cookie = key + "=" + value + ";" + expires;
	document.cookie = key + "=" + value + "; path=/";
}

function getCookie(key) {
	let cookie = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
	
	if (cookie !== null) {
		cookie = cookie[0].replace(key + "=", "");
		cookie = cookie.replace("Bearer ", "").replaceAll(";", "").replaceAll(" ", "");
	}
	
	return cookie;
}

function deleteCookie(key) {
	document.cookie = key + "=; expires=Thu, 01 Jan 1970 00:00:01 GMT;";
}

function logoutCookie(message) {
	let expiration = 'Sat, 01 Jan 1972 00:00:00 GMT';

	document.cookie = "authToken=; expires=" + expiration + "; path=" + "/";
	document.cookie = "role=; expires=" + expiration + "; path=" + "/";
	
	defaultErrorAlert(message).then(function() {
		window.location.replace("http://localhost:8080/login");
	});
}

function loginSetMenu() {
	$("li[class=1]").hide();
	$("li[class=2]").show();
}

function logoutSetMenu() {
	$("li[class=2]").hide();
	$("li[class=1]").show();
}

function back() {
	let href = $(location).attr('href').split("/");
	let origin = $(location).attr('origin');
		
	for(let i = 3; i < href.length - 1; i++){
		origin += "/" + href[i];
	}
		
	$(location).attr('href', origin);
}

function getUserPKAjax(authToken){
	$.ajax({
		url : "/token/pk",
		type : "get",
		beforeSend: function (xhr) {
		xhr.setRequestHeader("Authorization", "Bearer " + authToken);
		},
		success : function(res) {
			if (res.code === 200) {
				$("input[name=email]").val(res.data);
			}
		},
		error : function(res) {
			if (res.responseJSON.code === 400) {
				defaultErrorAlert(res.responseJSON.data.message, "실패");
			}
		}
	});
}

function licenseUp(data) {
    $(data).hide();
    $(data).siblings("i").show();
    $(data).parent().siblings("ul").slideUp();
}

function licenseDown(data) {
  	$(data).hide();
   	$(data).siblings("i").show();
   	$(data).parent().siblings("ul").slideDown();
}

function codeSendAjax(email) {
	$.ajax({
		url : "/api/authMail/send",
		type : "post",
		data : {email : email},
	    beforeSend: function() {
	        //마우스 커서를 로딩 중 커서로 변경
			$('html').css("cursor", "wait");
		},
	    success : function(res) {
			$('html').css("cursor", "auto");
			if (res.code === 200) {
				defaultSuccessAlert(res.message, "성공");
			}
		},
		error : function(res) {
			if (res.responseJSON.code === 400) {
				errorMessage(res.responseJSON.data);
			} else {
				defaultErrorAlert(res.responseJSON.message);
			}
		}
	});
}

function codeConfirmAjax(email, code) {
	$.ajax({
		url : "/api/authMail/confirm",
		type : "post",
		data : {
			email : email,
			code : code
			},
		success : function(res) {
			if (res.code === 200) {
				defaultSuccessAlert(res.message, "성공");
			}
		},
		error : function(res) {
			if (res.responseJSON.code === 400) {
				errorMessage(res.responseJSON.data);
			} else {
				defaultErrorAlert(res.responseJSON.message);
			}
		}
	});
}

function getSelectLicense(data) {
    let map = new Map();

    map.set(1, "lifelongSportsInstructor");
    map.set(2, "fitnessInstructor");
    map.set(4, "healthTrainer");

    map.forEach(function(value, key) {
        if ((key & data) !== 0) {
            $("input[id=" + value + "]").prop("checked", true);
        }
    });
}

function setList(list) {
	$("table[name=list]").find("tbody").find("tr").remove();

	let html = "";
	if (list.length === 0) {
		html += "<tr><td colspan=5 style='text-align: center;'> 조회된 결과가 없습니다. <td><tr>";
	} else {
		$.each(list, function(key, data) {
			html += "<tr>";
			html += "<td>" + data.title + "</td>";
			html += "<td>" + data.startDate + "</td>";
			html += "<td>" + data.endDate + "</td>";
			html += "<td>" + data.address + "</td>";
			html += "<td>";
			html += "<a href='/job/" + data.id + "' class='button small'>상세보기</a>";
			html += "</td>";
			html += "</tr>";
		});
	}

    $("table[name=list]").find("tbody").append(html);
}

function setPage(currentPage, totalPages) {
	$(".page").find(".p").remove();
	let appendHtml = "";

	for(let i = 0; i < totalPages; i++) {
		appendHtml += "<p class='p' id='" + i + "' onclick='getList(" + i + ")'>" + (i + 1) + "</p>";
	}

	$(".page").append(appendHtml);
	$("p[id=" + currentPage + "]").css("font-weight","600");
}