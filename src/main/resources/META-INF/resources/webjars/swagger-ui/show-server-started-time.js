document.addEventListener('DOMContentLoaded', () => {
  let startedAt = null;
  let newLine = null;
  let commitLine = null;
  let commitDate = null;
  let intervalId1 = null;
  let intervalId2 = null;
  let intervalId3 = null;

  const fetchGithubCommitTime = async () => {
    try {
      const res = await fetch('https://api.github.com/repos/CEC-project/CEC-Back/commits/main');
      const data = await res.json();
      commitDate = new Date(data.commit.committer.date);
    } catch (error) {
      console.error('GitHub 커밋 시간 가져오기 실패:', error);
    }
  };

  const updateRemainTimeToFetch = () => {
    if (!commitLine || !commitDate) return;

    const now = new Date();

    const diffMs = now - commitDate;
    const diffSec = Math.floor(diffMs / 1000);
    const diffMin = Math.floor(diffSec / 60);
    const diffHour = Math.floor(diffMin / 60);
    const diffDay = Math.floor(diffHour / 24);

    let lastCommitElapsed;
    if (diffDay >= 1) {
      lastCommitElapsed = `${diffDay}일 전`;
    } else if (diffHour >= 1) {
      lastCommitElapsed = `${diffHour}시간 전`;
    } else if (diffMin >= 1) {
      lastCommitElapsed = `${diffMin}분 전`;
    } else {
      lastCommitElapsed = `${diffSec}초 전`;
    }

    observer.disconnect();
    commitLine.innerHTML = `<strong>🧾 GitHub main 브랜치 마지막 커밋 : ${lastCommitElapsed} (30초 마다 갱신)</strong>`;
    observer.observe(target, {
      childList: true,
      subtree: true,
    });
  };

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

    observer.disconnect();
    newLine.innerHTML = `<strong>🚀 서버는 약 ${elapsed}에 시작되었습니다.</strong>`;
    observer.observe(target, {
      childList: true,
      subtree: true,
    });
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
    if (newLine && newLine.parentNode)
      newLine.parentNode.removeChild(newLine);
    if (commitLine && commitLine.parentNode)
      commitLine.parentNode.removeChild(commitLine);

    const originalP = descEl.closest('p');
    if (originalP)
      originalP.style.margin = '0';

    newLine = document.createElement('p');
    newLine.style.margin = '0';
    container.appendChild(newLine);
    commitLine = document.createElement('p');
    commitLine.style.margin = '0';
    container.appendChild(commitLine);

    // 이전 interval 제거
    if (intervalId1 !== null) clearInterval(intervalId1);
    if (intervalId2 !== null) clearInterval(intervalId2);
    if (intervalId3 !== null) clearInterval(intervalId3);

    updateElapsedTime();
    intervalId1 = setInterval(updateElapsedTime, 1000);
    fetchGithubCommitTime();
    intervalId2 = setInterval(fetchGithubCommitTime, 30000);
    updateRemainTimeToFetch();
    intervalId3 = setInterval(updateRemainTimeToFetch, 1000);
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