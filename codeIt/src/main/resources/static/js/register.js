/**
 * 
 */




$(document).ready(function() {
	
	let isIdAvailable = false;

	// ========== 공백 입력 차단 ==========
	$('#loginId, #memberPw, #confirmPw').on('input', function () {
	  const cleaned = $(this).val().replace(/\s/g, '');
	  $(this).val(cleaned);
	});

	// ========== 아이디 유효성 검사 + 중복 체크 ==========
	$('#loginId').on('blur', function () {
	  const loginId = $(this).val().trim();
	  const idRegex = /^[a-zA-Z0-9]{4,20}$/;
	  const idMsg = $('#idMsg');

	  $('#loginId').val(loginId);

	  if (!loginId) {
	    idMsg.text("").removeClass('valid');
	    isIdAvailable = false;
	    return;
	  }

	  if (!idRegex.test(loginId)) {
	    idMsg.text("아이디는 영문자+숫자 조합 4~20자여야 합니다.").removeClass('valid');
	    isIdAvailable = false;
	    return;
	  }

	  $.ajax({
	    type: "POST",
	    url: "/member/checkId",
	    contentType: "application/json",
	    data: JSON.stringify({ loginId }),
	    success: function (response) {
	      if (response === "duplicate") {
	        idMsg.text("이미 사용 중인 아이디입니다.").removeClass('valid');
	        isIdAvailable = false;
	      } else {
	        idMsg.text("사용 가능한 아이디입니다.").addClass('valid');
	        isIdAvailable = true;
	      }
	    },
	    error: function () {
	      idMsg.text("중복 체크 오류").removeClass('valid');
	      isIdAvailable = false;
	    }
	  });
	});

	// ========== 비밀번호 유효성 검사 ==========
	$('#memberPw').on('input', function () {
	  const pw = $(this).val();
	  const pwMsg = $('#pwMsg');
	  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[^\s]{8,}$/;

	  if (!pw) {
	    pwMsg.text("").removeClass('valid');
	    return;
	  }

	  if (!pwRegex.test(pw)) {
	    pwMsg.text("영문+숫자+특수문자 포함 8자 이상, 공백 없이 입력하세요.").removeClass('valid');
	  } else {
	    pwMsg.text("사용 가능한 비밀번호입니다.").addClass('valid');
	  }
	});

	// ========== 비밀번호 확인 검사 ==========
	$('#confirmPw, #memberPw').on('input', function () {
	  const pw = $('#memberPw').val();
	  const confirm = $('#confirmPw').val();
	  const msg = $('#confirmMsg');

	  if (!confirm) {
	    msg.text("").removeClass('valid');
	    return;
	  }

	  if (pw !== confirm) {
	    msg.text("비밀번호가 일치하지 않습니다.").removeClass('valid');
	  } else {
	    msg.text("비밀번호가 일치합니다.").addClass('valid');
	  }
	});

	// ========== 이름 유효성 검사 ==========
	$('#memberName').on('input', function () {
	  const name = $(this).val().trim();
	  const nameRegex = /^[가-힣a-zA-Z]{2,}$/;
	  const msg = $('#nameMsg');

	  $('#memberName').val(name);

	  if (!name) {
	    msg.text("").removeClass('valid');
	    return;
	  }

	  if (!nameRegex.test(name)) {
	    msg.text("이름은 한글 또는 영문 2자 이상이어야 합니다.").removeClass('valid');
	  } else {
	    msg.text("사용 가능한 이름입니다.").addClass('valid');
	  }
	});

	// ========== 이메일 유효성 검사 ==========
	$('#memberEmail').on('input', function () {
	  const email = $(this).val().trim();
	  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	  const msg = $('#emailMsg');

	  $('#memberEmail').val(email);

	  if (!email) {
	    msg.text("").removeClass('valid');
	    return;
	  }

	  if (!emailRegex.test(email)) {
	    msg.text("올바른 이메일 형식이 아닙니다.").removeClass('valid');
	  } else {
	    msg.text("사용 가능한 이메일입니다.").addClass('valid');
	  }
	});

	// ========== 최종 제출 전 유효성 검사 ==========
	$('.register-form').on('submit', function (e) {
	  const loginId = $('#loginId').val().trim();
	  const pw = $('#memberPw').val();
	  const confirm = $('#confirmPw').val();
	  const name = $('#memberName').val().trim();
	  const email = $('#memberEmail').val().trim();
	  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[^\s]{8,}$/;

	  $('#loginId').val(loginId);
	  $('#memberName').val(name);
	  $('#memberEmail').val(email);

	  if (!isIdAvailable) {
	    alert("아이디 중복 확인이 필요합니다.");
	    e.preventDefault();
	    return;
	  }

	  if (!pwRegex.test(pw)) {
	    alert("비밀번호 형식을 확인해주세요.");
	    e.preventDefault();
	    return;
	  }

	  if (pw !== confirm) {
	    alert("비밀번호가 일치하지 않습니다.");
	    e.preventDefault();
	    return;
	  }

	  if (!name) {
	    alert("이름을 입력해주세요.");
	    e.preventDefault();
	    return;
	  }

	  if (!email) {
	    alert("이메일을 입력해주세요.");
	    e.preventDefault();
	    return;
	  }

	  if (!$("input[name='termsAgreed']").is(":checked")) {
	    alert("이용약관에 동의해야 가입할 수 있습니다.");
	    e.preventDefault();
	  }
	});
	
	
	document.getElementById("openTerms").onclick = function(e) {
		  e.preventDefault();
		  document.getElementById("termsModal").style.display = "block";
		};
		
		document.querySelector(".close").onclick = function() {
		  document.getElementById("termsModal").style.display = "none";
		};
		
		window.onclick = function(event) {
		  if (event.target === document.getElementById("termsModal")) {
		    document.getElementById("termsModal").style.display = "none";
		  }
		};
		
		$('#sendCodeBtn').on('click', function () {
		    const email = $('#memberEmail').val();
		    if (!email) {
		      alert('이메일을 입력해주세요.');
		      return;
		    }

		    $.post('/member/send-auth-email', { email })
		      .done(function (res) {
		        alert(res);
		        $('.email-code-box').show();
		      })
		      .fail(function () {
		        alert('인증 메일 전송 실패');
		      });
		  });

		  $('#verifyCodeBtn').on('click', function () {
		    const email = $('#memberEmail').val();
		    const code = $('#authCode').val();

		    if (!code) {
		      alert('인증 코드를 입력해주세요.');
		      return;
		    }

		    $.post('/member/verify-auth-code', { email, code })
		      .done(function (res) {
		        $('#verifyMessage').text(res);
		        if (res.includes("성공")) {
		          $('#verifyMessage').css("color", "green");
		        } else {
		          $('#verifyMessage').css("color", "red");
		        }
		      })
		      .fail(function () {
		        alert('인증 확인 실패');
		      });
		  });
	
	
	
	
	
	
});