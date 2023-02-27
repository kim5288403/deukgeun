function defalutSuccessAlert(text, title) {
	return Swal.fire({
		title : title ? title : "success",
		text : text,
		icon : "success",
		confirmButtonText: "확인"
		});
}

function defalutErrorAlert(text, title) {
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
	document.cookie = key + "=" + value;
}

function getCookie(key) {
	let cookie = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
	return cookie !== null ? cookie[0].replace(key + "=", "") : null;
}

function deleteCookie(key) {
	document.cookie = key + "=; expires=Thu, 01 Jan 1970 00:00:01 GMT;";
}

function logoutCookie(message) {
	let expiration = 'Sat, 01 Jan 1972 00:00:00 GMT';
	document.cookie = "authToken=; expires=" + expiration;
	document.cookie = "role=; expires=" + expiration;
	document.cookie = "authToken=; expires=" + expiration + "; path=" + "/";
	document.cookie = "role=; expires=" + expiration + "; path=" + "/";
	
	defalutErrorAlert(message).then(function() {
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
	