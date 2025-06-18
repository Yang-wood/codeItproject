/**
 * 
 */




	$(document).ready(function () {
		const couponList = Array.isArray(window.couponList) ? window.couponList : [];
		const itemId = couponList.length > 0 ? couponList[0].itemId : null;
		const couponCode = couponList.length > 0 ? couponList[0].couponCode : null;
		const isAdmin = window.isAdmin;
		const memberId = window.memberId;
		const adminId = window.adminId || null;
		
		console.log("쿠폰 리스트:", couponList);
		console.log("itemId:", itemId, "couponCode:", couponCode);
		
		if (memberId === null && adminId === null) {
			alert("로그인이 필요한 서비스 입니다");
			location.href = '/member/login';
			
			return;
		}
		
		
		
		
		
		function loadTests(category = 'all', page = 1) {
		   $.ajax({
				url: "/api/test/",
				method: "GET",
				data: { category, page },
				success: function (data) {
					console.log("시험 목록 데이터 : ", data);
					
					const testList = $('#testList');
					testList.empty();
					
					if (data.content.length === 0) {
						testList.append('<p>해당 카테고리의 시험이 없습니다.</p>');
						return;
					}
		
					data.content.forEach(function (test) {
						
						const visibility = test.isOpen === 'Y' ? '비공개' : '공개';
						
						testList.append(`
							<div class="test-item" data-testid="${test.testId}" data-isopen="${test.isOpen}">
								<div class="test-info">
									<div class="test-title" style="color:#365cff;">
										${test.testTitle}
									</div>
									<div class="test-desc">${test.testDesc || '설명이 없습니다.'}</div>
									<div class="test-meta">
										시간: ${test.testLimit}분 | 조회수: ${test.viewCnt}
									</div>
								</div>
								<div class="test-actions">
									<button class="btn-start">바로응시</button>
									<div class="admin-actions" style="margin-top:8px; display:none;">
										<button class="btn-modify" style="background:#ffa500; color:white;">수정</button>
									</div>
									<div class="admin-actions" style="margin-top:8px; display:none;">
										<button class="btn-toggle" style="background:#ff3b3b; color:white;">${visibility}</button>
									</div>
								</div>
							</div>
						`);
					});
					
					if (isAdmin) {
						$('.admin-actions').show();
					}
			
					const pagination = $('#pagination');
					
					pagination.empty();
					
					for (let i = 1; i <= data.totalPages; i++) {
					  const active = i === page ? 'style="font-weight:bold;"' : '';
					  pagination.append(`<button ${active} onclick="loadTests('${category}', ${i})">${i}</button>`);
					}
		     	},
				error: function (err) {
				  alert("시험 목록을 불러오는 데 실패했습니다.");
				  console.error(err);
				}
			});
		}
		
		
		// 테스트 수정
		$(document).on("click", ".btn-modify", function() {
			const testId = $(this).closest(".test-item").data("testid");
			window.location.href = `/omr/admin/modify?testId=${testId}`;
		});
		
		
		
		// 시험지 비공개
		$(document).on("click", ".btn-toggle", function() {
			const item = $(this).closest(".test-item");
			const testId = item.data("testid");
			const isOpen = item.data("isopen");
			
			const action = isOpen === 'Y' ? "비공개" : "공개";
			
			if (confirm(`이 시험지를 ${action} 처리하시겠습니까?`)) {
				$.ajax({
					url : "/api/toggle",
					type : "POST",
					contentType : "application/json",
					data : JSON.stringify({ testId: testId }),
					success : function() {
						alert(`${action} 처리되었습니다`);
						location.reload();
					},
					error : function() {
						alert(`${action} 처리 실패`);
					}
				});
			}
		});
		
	 	
	  
	  
	 	
		$('#categoryFilter').on('change', function () {
			loadTests($(this).val(), 1);
		});
	
		loadTests();
	
	   
	   
		// 바로응시 버튼 클릭시 안내 모달
		$(document).on("click", ".btn-start", function(e) {
			e.preventDefault();
			e.stopPropagation();
			
			if ($("#testModal").is(":visible")) return;
			
			const testTitle = $(this).closest(".test-item").find(".test-title").text();
			const testId = $(this).closest(".test-item").data("testid");
			
			$("#testModal").data("testid", testId).fadeIn();
			$("#modalOverlay").fadeIn();
		});
	  
	  
		// 모달 닫기 버튼
		$("#closeModal, #modalOverlay").on("click", function() {
			$("#testModal").fadeOut();
			$("#modalOverlay").fadeOut();
		});
	  
	  
	  
	  
		let currentQuestionIndex = 0;
		let examQuestions = []; // 서버에서 받아올 문제 목록, 선택 답안
	
		function renderQuestion(index) {
			const question = examQuestions[index];
			
			let html = `<h3>문제 ${index + 1} / ${examQuestions.length}</h3>`;
			html += `<p>${question.questionText}</p>`;
			
			let choices = [];
			
			console.log("choiceJson : ", question.choiceJson);
			
			try {
				const parsed = JSON.parse(question.choiceJson); 
				
				choices = Array.isArray(parsed) ? parsed : Object.values(parsed);
			} catch (e) {
				console.error("보기 파싱 실패:", e);
			}
			
			html += choices.map((choice, i) => {
				const realVal = i + 1;
				const checked = question.userAnswer == realVal ? 'checked' : '';
				return `<div>
							<label><input type="radio" name="answer${index}" value="${realVal}" ${checked}> ${choice}</label>
						</div>`;
			}).join('');
			
			$("#examContent").html(html);
			
			$("#prevBtn").toggle(index > 0);
			$("#nextBtn").toggle(index < examQuestions.length - 1);
			$("#submitBtn").toggle(index === examQuestions.length - 1);
			
			
			// 답안 수정 시 저장된 값 변경
			$(`input[name=answer${index}]`).on("change", function() {
				const selectedVal = $(this).val();
				
				examQuestions[index].userAnswer = selectedVal;
			});
			
		}
		
		
		// 타이머
		let startTime = null;
		let timerInterval = null;
		
		
		function startExam(testId) {
			console.log("startExam 호출, testId : ", testId);

			$.ajax({
				url: `/api/exam/start/${testId}`,
				method: "GET",
				success: function (data) {
					console.log("랜덤 문제 데이터 : ", data);
				  
					examQuestions = data.slice(0, 10); // 최대 20문제만
					currentQuestionIndex = 0;
					
					// 타이머
					startTime = new Date();
					timerInterval = setInterval(updateTimer, 1000);
					
					$("#examModal").fadeIn();
					renderQuestion(currentQuestionIndex);
				},
				error: function () {
					alert("시험 문제를 불러오는 데 실패했습니다.");
					$("#testModal").hide();
					$("#modalOverlay").hide();
				}
			});
			
			$.ajax({
				url: "/api/exam/start",
				method: "POST",
				contentType: "application/json",
				data: JSON.stringify({
					memberId: memberId,
					testId: testId,
					itemId: itemId,
					couponCode: couponCode
				}),
				success: function(sessionId) {
					window.sessionId = sessionId;
				},
				error : function(xhr) {
					$("#testModal").hide();
					$("#modalOverlay").hide();
					
					if(xhr.status === 403) {
						alert(xhr.responseText);
						location.href = `/vending/main`;
					} else if (xhr.status === 400 && xhr.responseText.includes("쿠폰")) {
						alert("사용 가능한 쿠폰이 없습니다. 자판기에서 쿠폰을 뽑아주세요.");
						location.href = `/vending/main`;
					} else {
						alert("시험 시작 실패: " + xhr.responseText);
					}
				}
			});
			
		}
		
		
		
		function updateTimer() {
			const now = new Date();
			const elapsed = Math.floor((now - startTime) / 1000);
			
			const min = String(Math.floor(elapsed / 60)).padStart(2, '0');
			const sec = String(elapsed % 60).padStart(2, '0');
			
			$("#timer").text(`경과 시간 : ${min}:${sec}`);
		}
		
		
	
		$("#startConfirm").on("click", function () {
			const testId = $("#testModal").data("testid");
			$("#testModal").fadeOut();
			$("#modalOverlay").fadeOut();
			startExam(testId);
		});
		
		$("#nextBtn").on("click", function () {
			// 답안 저장
			const selectedVal = $(`input[name=answer${currentQuestionIndex}]:checked`).val();
			
			if (selectedVal !== undefined) {
				examQuestions[currentQuestionIndex].userAnswer = selectedVal;
			}
			
			// 문제 넘기기
			if (currentQuestionIndex < examQuestions.length - 1) {
				currentQuestionIndex++;
				renderQuestion(currentQuestionIndex);
			}
		});
		
		$("#prevBtn").on("click", function () {
			// 답안 저장
			const selectedVal = $(`input[name=answer${currentQuestionIndex}]:checked`).val();
			
			if (selectedVal !== undefined) {
				examQuestions[currentQuestionIndex].userAnswer = selectedVal;
			}
			
			// 문제 되돌리기
			if (currentQuestionIndex > 0) {
				currentQuestionIndex--;
				renderQuestion(currentQuestionIndex);
			}
		});
		
		$("#submitBtn").on("click", function () {
			// 타이머 스탑
			clearInterval(timerInterval);
			const endTime = new Date();
			const durationSec = Math.floor((endTime - startTime) / 1000);
			
			// 답안 저장
			const selectedVal = $(`input[name=answer${currentQuestionIndex}]:checked`).val();
			
			if (selectedVal !== undefined) {
				examQuestions[currentQuestionIndex].userAnswer = selectedVal;
			}
			
			const answers = examQuestions.map((q) => ({
				 questionId: q.questionId,
				 choiceAnswer: q.userAnswer !== undefined ? `${q.userAnswer}`.charAt(0) : null
			}));
			
			// 경과시간, 답안, 세션 제출
			const payload = {
					sessionId: window.sessionId,
					answers: answers,
					duration: durationSec
			};
			
			
			console.log("제출할 답안 데이터 : ", answers);
			
			$.ajax({
				url: "/api/exam/submit",
				method: "POST",
				contentType: "application/json",
				data: JSON.stringify(payload),
				success: function (res) {
					alert(res);
					
					$("#examModal").fadeOut();
				},
				error: function (xhr) {
					alert("제출 실패!" + xhr.status);
					console.error(xhr.responseText);
				}
			});
		});
		
		
		
		
		
		
		
    });
