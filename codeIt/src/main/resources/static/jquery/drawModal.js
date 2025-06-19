let currentMachineId = null;
let autoRetryTimer = null;

function confirmDraw(machineId, itemId) {
  currentMachineId = machineId;
  const itemIds = [itemId];

  fetch(`/api/member/vendingmachines/${machineId}/purchase`, {
    method: 'POST',
    body: JSON.stringify({ itemIds }),
    headers: { 'Content-Type': 'application/json' }
  })
  .then(res => {
    if (!res.ok) throw new Error("응답 실패");
    return res.json();
  })
  .then(result => {
    if (result.error === 'NOT_ENOUGH_POINT') {
      stopAutoRetry();
      showErrorModal("포인트가 부족하여 뽑기를 중단합니다.");
      return;
    }
    showResultModal(result);
    fetchRecentHistory();
    fetchPoint();
  })
  .catch(err => {
    stopAutoRetry();
	alert("⚠️ 결제 실패: " + err.message);
  });
}

function showResultModal(result) {
  const modal = document.getElementById("result-modal");
  const body = document.getElementById("modal-body");
  const actions = document.getElementById("modal-actions");

  if (result.itemName === '꽝') {
    body.innerHTML = `<h2 style='color: gray;'>💔 아쉽지만 꽝!</h2><p>다시 도전해보세요!</p>`;
    actions.innerHTML = `<button onclick='confirmDraw(${currentMachineId}, ${result.itemId})'>🔁 다시 도전</button>`;
  } else {
    body.innerHTML = `<h2 style='color: green;'>🎉 축하합니다!</h2>
      <p><strong>${result.itemName}</strong> (${result.itemType})</p>
      ${result.couponCode ? `<p>쿠폰 코드: <strong>${result.couponCode}</strong></p>` : ''}`;
    actions.innerHTML = `
      <button onclick='confirmDraw(${currentMachineId}, ${result.itemId})'>🔁 다시 도전</button>
      <button onclick='toggleAutoRetry(this, ${result.itemId})'>⏩ 자동 재도전</button>`;
  }

  modal.style.display = "flex";
}

function showErrorModal(message) {
  const modal = document.getElementById("result-modal");
  const body = document.getElementById("modal-body");
  const actions = document.getElementById("modal-actions");

  body.innerHTML = `<p style='color:red;'>${message}</p>`;
  actions.innerHTML = `<button onclick='closeModal()'>닫기</button>`;

  modal.style.display = "flex";
}

function closeModal() {
  document.getElementById("result-modal").style.display = "none";
}

function toggleAutoRetry(btn, itemId) {
  if (autoRetryTimer) {
    clearInterval(autoRetryTimer);
    autoRetryTimer = null;
    btn.innerText = "⏩ 자동 재도전";
  } else {
    autoRetryTimer = setInterval(() => confirmDraw(currentMachineId, itemId), 2000);
    btn.innerText = "⏹ 자동 중지";
  }
}

function stopAutoRetry() {
  if (autoRetryTimer) {
    clearInterval(autoRetryTimer);
    autoRetryTimer = null;
    const retryBtn = document.querySelector("#modal-actions button:nth-child(2)");
    if (retryBtn) retryBtn.innerText = "⏩ 자동 재도전";
  }
}

function fetchPoint() {
  fetch('/api/member/points')
    .then(res => res.json())
    .then(data => {
      const pointArea = document.getElementById("pointArea");
      if (pointArea) {
        pointArea.textContent = data.points + " P";
      }
    });
}

function fetchRecentHistory() {
  fetch(`/admin/vending/history/{machineId}`)
    .then(res => res.json())
    .then(history => {
      const historyBox = document.getElementById("recentHistory");
      if (historyBox) {
        historyBox.innerHTML = history.map(item => `
          <div>[${item.time}] <strong>${item.memberName}</strong>님이 <em>${item.itemName}</em>을 뽑았습니다!</div>
        `).join('');
      }
    });
}
