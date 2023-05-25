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
		url : "/jwt/pk",
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
	