/**
 * 
 */




function openFindIdModal() {
  document.getElementById("findIdModal").style.display = "flex";
}
function closeFindIdModal() {
  document.getElementById("findIdModal").style.display = "none";
}
function findId() {
  const name = document.getElementById("findName").value;
  const email = document.getElementById("findEmail").value;

  fetch('/member/ajax/find-id', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ memberName: name, memberEmail: email })
  })
  .then(res => res.json())
  .then(data => {
    const resultDiv = document.getElementById("foundId");
    if (data.result === 'success') {
      resultDiv.textContent = "아이디: " + data.loginId;
    } else {
      resultDiv.textContent = "일치하는 회원이 없습니다.";
    }
  });
}

function openFindPwModal() {
  document.getElementById("findPwModal").style.display = "flex";
}
function closeFindPwModal() {
  document.getElementById("findPwModal").style.display = "none";
}
function findPw() {
  const loginId = document.getElementById("findPwId").value;
  const email = document.getElementById("findPwEmail").value;

  fetch('/member/ajax/find-pw', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ loginId, memberEmail: email })
  })
  .then(res => res.json())
  .then(data => {
    const resultDiv = document.getElementById("pwResult");
    resultDiv.textContent = data.msg;
  });
}