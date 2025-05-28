document.addEventListener('DOMContentLoaded', () => {
  let startedAt = null;
  let newLine = null;
  let intervalId = null;

  const updateElapsedTime = () => {
    if (!startedAt || !newLine) return;

    const now = new Date();
    const diffMs = now - startedAt;
    const diffSec = Math.floor(diffMs / 1000);
    const diffMin = Math.floor(diffSec / 60);
    const diffHour = Math.floor(diffMin / 60);
    const diffDay = Math.floor(diffHour / 24);

    let elapsed;
    if (diffDay >= 1) {
      elapsed = `${diffDay}일 전`;
    } else if (diffHour >= 1) {
      elapsed = `${diffHour}시간 전`;
    } else if (diffMin >= 1) {
      elapsed = `${diffMin}분 전`;
    } else {
      elapsed = `${diffSec}초 전`;
    }

    newLine.innerHTML = `<strong>🚀 서버는 약 ${elapsed}에 시작되었습니다.</strong>`;
  };

  const initialize = () => {
    const descEl = document.querySelector('.info .renderedMarkdown p strong');
    if (!descEl) return;

    const descText = descEl.textContent;
    const matched = descText.match(/\s*:\s*(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
    if (!matched) return;

    startedAt = new Date(matched[1].replace(' ', 'T'));
    const container = descEl.closest('.renderedMarkdown');
    if (!container) return;

    // 기존 줄 제거 (중복 방지)
    if (newLine && newLine.parentNode) {
      newLine.parentNode.removeChild(newLine);
    }

    const originalP = descEl.closest('p');
    if (originalP)
      originalP.style.margin = '0';

    newLine = document.createElement('p');
    newLine.style.margin = '0';
    container.appendChild(newLine);

    // 이전 interval 제거
    if (intervalId !== null) {
      clearInterval(intervalId);
    }

    updateElapsedTime();
    intervalId = setInterval(updateElapsedTime, 1000);
  };

  const observer = new MutationObserver(() => {
    // 옵저버를 잠시 끊고 초기화 수행
    observer.disconnect();
    initialize();
    // 다시 옵저빙 시작
    observer.observe(target, {
      childList: true,
      subtree: true,
    });
  });

  const target = document.getElementById('swagger-ui');
  if (target) {
    observer.observe(target, {
      childList: true,
      subtree: true,
    });
  }

  setTimeout(() => {
    observer.disconnect();
    initialize();
    observer.observe(target, {
      childList: true,
      subtree: true,
    });
  }, 500);
});