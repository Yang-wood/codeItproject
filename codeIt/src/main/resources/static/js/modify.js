/**
 * 
 */




	
	function updateName() {
	  const name = document.getElementById("newName").value;
	  fetch("/member/ajax/update-name", {
	    method: "POST",
	    headers: { 'Content-Type': 'application/json' },
	    body: JSON.stringify({ memberName: name })
	  })
	  .then(res => res.json())
	  .then(data => alert(data.msg));
	}

	function sendEmailCode() {
	  const email = document.getElementById("newEmail").value;
	  fetch("/member/send-auth-email", {
	    method: "POST",
	    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
	    body: new URLSearchParams({ email })
	  })
	  .then(res => res.text())
	  .then(alert);
	}

	function verifyEmail() {
		  const email = document.getElementById("newEmail").value;
		  const code = document.getElementById("emailCode").value;

		  fetch("/member/verify-auth-code", {
		    method: "POST",
		    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		    body: new URLSearchParams({ email, code })
		  })
		  .then(res => res.text())
		  .then(result => {
		    alert(result);

		    if (result.includes("성공")) {
		      // 인증에 성공했으면 이메일 실제로 변경 요청
		      fetch("/member/ajax/update-email", {
		        method: "POST",
		        headers: { 'Content-Type': 'application/json' },
		        body: JSON.stringify({ newEmail: email })
		      })
		      .then(res => res.json())
		      .then(data => {
		        alert(data.msg);
		        if (data.result === "success") {
		          location.reload(); // 변경 후 새로고침
		        }
		      });
		    }
		  });
		}

	function updatePassword() {
	  const currentPw = document.getElementById("currentPw").value;
	  const newPw = document.getElementById("newPw").value;
	  const confirmPw = document.getElementById("confirmPw").value;
	  
	  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[^\s]{8,}$/;

	  if (!pwRegex.test(newPw)) {
	    alert("비밀번호는 영문+숫자+특수문자 포함 8자 이상이어야 합니다.");
	    return;
	  }
	  
	  if (newPw !== confirmPw) {
	    alert("새 비밀번호가 일치하지 않습니다.");
	    return;
	  }

	  fetch("/member/ajax/update-password", {
	    method: "POST",
	    headers: { 'Content-Type': 'application/json' },
	    body: JSON.stringify({ currentPw, newPw })
	  })
	  .then(res => res.json())
	  .then(data => alert(data.msg));
	}
	
	
	
	
	
