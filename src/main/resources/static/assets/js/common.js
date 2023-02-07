function defalutSuccessAlert(text, title) {
	Swal.fire({
		title : title ? title : "success",
		text : text,
		icon : "success",
		confirmButtonText: "확인"
		}).then(function () {
			window.location.replace("http://localhost:8080/");
		});
}