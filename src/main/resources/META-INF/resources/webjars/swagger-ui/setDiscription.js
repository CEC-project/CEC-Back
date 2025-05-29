// 서버 주소 키 정의
const docExpansionKey = 'docExpansion';
const defaultModelRenderingKey = 'defaultModelRendering';

const addUI = (container) => {
  const settingsWrapper = document.createElement('div');
  settingsWrapper.style.display = 'flex';
  settingsWrapper.style.flexDirection = 'column';
  settingsWrapper.style.gap = '8px';
  settingsWrapper.style.position = 'absolute';
  settingsWrapper.style.top = '0';
  settingsWrapper.style.right = '0';

  const expansionWrapper = document.createElement('div');
  expansionWrapper.style.display = 'flex';
  expansionWrapper.style.alignItems = 'center';

  expansionWrapper.innerHTML = `
  <label for="doc-expansion" class="servers-title">전체 펼치기/접기 설정</label>
  <select id="doc-expansion" style="margin-left: 8px;">
    <option value="list">펼치기</option>
    <option value="none">접기</option>
  </select>`;

  const themeWrapper = document.createElement('div');
  themeWrapper.style.display = 'flex';
  themeWrapper.style.alignItems = 'center';

  themeWrapper.innerHTML = `
  <label for="default-model" class="servers-title">응답 바디 기본값 설정</label>
  <select id="default-model" style="margin-left: 8px;">
    <option value="example">example</option>
    <option value="model">schema</option>
  </select>`;

  settingsWrapper.appendChild(expansionWrapper);
  settingsWrapper.appendChild(themeWrapper);

  container.style.position = 'relative';
  container.appendChild(settingsWrapper);

  const expansionSelect = expansionWrapper.querySelector('select');
  const themeSelect = themeWrapper.querySelector('select');
  expansionSelect.value = localStorage.getItem(docExpansionKey) || 'list';
  themeSelect.value = localStorage.getItem(defaultModelRenderingKey) || 'example';

  expansionSelect.addEventListener('change', (e) => {
    localStorage.setItem(docExpansionKey, e.target.value);
    window.location.reload();
  });

  themeSelect.addEventListener('change', (e) => {
    localStorage.setItem(defaultModelRenderingKey, e.target.value);
    window.location.reload();
  });
};

function setDiscription () {
  let startedAt = null;
  let newLine = null;
  let intervalId = null;

  const fetchServerTime = async () => {
    try {
      fetch('/api/health-check')
      .then(res => res.json())
      .then(data => {
        const newServerStartAt = new Date(data.data.replace(' ', 'T')); // ISO 포맷으로 변환
        if (newServerStartAt.getTime() !== startedAt.getTime()) {
          newLine.innerHTML += `<br><strong>새 서버가 시작된 시각 : ${data.data}</strong>`;
          newLine.innerHTML += `<br><strong>새로고침 하시기 바랍니다</strong>`;
          clearInterval(intervalId);
        }
      });
    } catch (error) {
      console.error('서버 시간 가져오기 실패:', error);
    }
  };

  const initialize = () => {

    // 서버 시간 가져오기
    fetch('/api/health-check')
    .then(res => res.json())
    .then(data => {
      startedAt = new Date(data.data.replace(' ', 'T')); // ISO 포맷으로 변환

      // description 박스가 없는 경우 종료
      const container = document.querySelector('.description');
      if (!container) return;

      const markdown = document.createElement('div');
      markdown.className = 'renderedMarkdown';

      // 서버 시작 시각
      newLine = document.createElement('p');
      newLine.style.margin = '0';
      newLine.innerHTML = `<strong>새로고침해도 로그인 상태 유지됩니다.</strong>`;
      newLine.innerHTML += `<br><strong>서버가 시작된 시각 : ${data.data}</strong>`;
      markdown.appendChild(newLine);
      container.appendChild(markdown);

      addUI(container);

      // 인터벌 초기화
      if (intervalId !== null) clearInterval(intervalId);

      fetchServerTime();
      intervalId = setInterval(fetchServerTime, 30000);
    })
    .catch(err => {
      console.error('서버 시간 fetch 실패:', err);
    });
  };

  initialize();
}