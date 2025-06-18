/**
 * 
 */




	$(document).ready(function () {
		const couponList = Array.isArray(window.couponList) ? window.couponList : [];
		const itemId = couponList.length > 0 ? couponList[0].itemId : null;
		const couponCode = couponList.length > 0 ? couponList[0].couponCode : null;
		const memberId = window.memberId;
		const adminId = window.adminId;
		
		console.log("쿠폰 리스트:", couponList);
		console.log("itemId:", itemId, "couponCode:", couponCode);
		console.log("adminId : ", adminId);
		
		if (memberId === null && adminId === null) {
			alert("로그인이 필요한 서비스 입니다");
			location.href = '/member/login';
			
			return;
		}
		
		
		
		
    });
