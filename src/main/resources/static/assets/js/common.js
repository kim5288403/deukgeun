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
	document.cookie = key + "=" + value;
}

function getCookie(key) {
	let cookie = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
	return cookie !== null ? cookie[0].replace(key + "=", "") : null;
}
	