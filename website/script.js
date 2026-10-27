const signupForm = document.querySelector('.signup-form');
if (signupForm) {
  signupForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const button = this.querySelector('button');
    button.innerHTML = 'You’re on the list <span>✓</span>';
    button.disabled = true;
  });
}

const heroSocials = document.querySelector('.hero .social-actions');
if (heroSocials) heroSocials.remove();

const socialIcons = document.querySelector('.masthead-actions .social-icons');
const mastheadActions = document.querySelector('.masthead-actions');
const writeForUsLink = document.querySelector('.write-link');
if (writeForUsLink) writeForUsLink.remove();
const workspaceLabel = document.querySelector('.workspace-intro .section-number');
if (workspaceLabel) workspaceLabel.textContent = 'TCPRI TEAM WORKSPACE';
const liveStatus = document.querySelector('.workspace-panel .status-dot');
if (liveStatus) liveStatus.remove();
const joinLabel = document.querySelector('.join-cta .section-number');
if (joinLabel) joinLabel.textContent = 'JOIN TCPRI';
document.querySelectorAll('a[href^="http://localhost:8080"]').forEach(function (link) {
  link.target = '_blank';
  link.rel = 'noopener noreferrer';
});
if (mastheadActions && !mastheadActions.querySelector('.search-masthead-link')) {
  const searchLink = document.createElement('a');
  searchLink.className = 'search-masthead-link';
  searchLink.href = '#search';
  searchLink.innerHTML = '<svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="10.5" cy="10.5" r="6.5" fill="none" stroke="currentColor" stroke-width="2"/><path d="m16 16 5 5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg><span>Search TCPRI</span>';
  mastheadActions.insertBefore(searchLink, mastheadActions.querySelector('.social-icons'));
}
if (socialIcons) {
  const instagram = socialIcons.querySelector('a[aria-label="TCPRI Instagram"]');
  if (instagram) {
    instagram.innerHTML = '<svg viewBox="0 0 24 24" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="5" fill="none" stroke="currentColor" stroke-width="2"/><circle cx="12" cy="12" r="4" fill="none" stroke="currentColor" stroke-width="2"/><circle cx="17.5" cy="6.5" r="1" fill="currentColor"/></svg>';
  }
  if (!socialIcons.querySelector('a[aria-label="TCPRI LinkedIn"]')) {
    const linkedin = document.createElement('a');
    linkedin.href = 'https://www.linkedin.com/company/tcpri-takshasila-centre-for-policy-research-and-innovation/';
    linkedin.target = '_blank';
    linkedin.rel = 'noopener noreferrer';
    linkedin.setAttribute('aria-label', 'TCPRI LinkedIn');
    linkedin.textContent = 'in';
    socialIcons.insertBefore(linkedin, socialIcons.lastElementChild);
  }
}

const footerLinks = document.querySelector('.footer-links');
if (footerLinks && !footerLinks.querySelector('a[href*="linkedin.com/company/tcpri"]')) {
  const linkedin = document.createElement('a');
  linkedin.href = 'https://www.linkedin.com/company/tcpri-takshasila-centre-for-policy-research-and-innovation/';
  linkedin.target = '_blank';
  linkedin.rel = 'noopener noreferrer';
  linkedin.textContent = 'LinkedIn / TCPRI ↗';
  footerLinks.appendChild(linkedin);
}

const workspaceSection = document.querySelector('#workspace');
const mainNav = document.querySelector('.main-nav');
if (mainNav && !mainNav.querySelector('a[href="#search"]')) {
  const searchNavLink = document.createElement('a');
  searchNavLink.href = '#search';
  searchNavLink.textContent = 'Search';
  mainNav.appendChild(searchNavLink);
}
if (workspaceSection) {
  const articles = [
    { title: 'India and the future of public policy', topic: 'Public Policy · Research', description: 'Evidence-led perspectives on the systems and decisions shaping India.', url: 'https://www.instagram.com/tcpri.in' },
    { title: 'Geopolitics in a changing world', topic: 'Geopolitics · International Relations', description: 'Research and analysis on emerging national and international challenges.', url: 'https://www.instagram.com/tcpri.in' },
    { title: 'Climate, governance and resilient growth', topic: 'Climate Change · Governance', description: 'Exploring practical policy ideas for an inclusive and sustainable future.', url: 'https://www.instagram.com/tcpri.in' }
  ];
  const searchSection = document.createElement('section');
  searchSection.className = 'article-search';
  searchSection.id = 'search';
  searchSection.innerHTML = '<div class="article-search-inner shell"><p class="section-number">SEARCH TCPRI</p><h2>Find an article<br /><span>worth exploring.</span></h2><p class="article-search-intro">Search TCPRI research topics, policy questions, and published ideas. Select a result to open its source page.</p><form class="article-search-form" role="search"><input type="search" id="article-search-input" placeholder="Search articles, topics or keywords" autocomplete="off" /><button type="submit">Search <span>↗</span></button></form><div class="search-results" aria-live="polite"><p class="search-empty">Try a topic such as policy, geopolitics, or climate.</p></div></div>';
  workspaceSection.before(searchSection);

  const searchForm = searchSection.querySelector('.article-search-form');
  const searchInput = searchSection.querySelector('#article-search-input');
  const results = searchSection.querySelector('.search-results');
  const renderResults = () => {
    const query = searchInput.value.trim().toLowerCase();
    if (!query) {
      results.innerHTML = '<p class="search-empty">Try a topic such as policy, geopolitics, or climate.</p>';
      return;
    }
    const matches = articles.filter(article => `${article.title} ${article.topic} ${article.description}`.toLowerCase().includes(query));
    results.innerHTML = matches.length ? matches.map(article => `<article class="search-result"><div><p class="section-number">${article.topic}</p><h3>${article.title}</h3><p>${article.description}</p></div><a class="arrow-link" href="${article.url}" target="_blank" rel="noopener noreferrer">Open source <span>↗</span></a></article>`).join('') : '<p class="search-empty">No TCPRI articles matched that search.</p>';
  };
  searchInput.addEventListener('input', renderResults);
  searchForm.addEventListener('submit', event => { event.preventDefault(); renderResults(); });
}

const latestLink = document.querySelector('.hero-actions .text-link');
document.querySelectorAll('a[href="#search"]').forEach(function (link) { link.remove(); });
document.querySelectorAll('.article-search').forEach(function (section) { section.remove(); });
if (latestLink) {
  latestLink.href = 'https://www.instagram.com/tcpri.in';
  latestLink.target = '_blank';
  latestLink.rel = 'noopener noreferrer';
  latestLink.innerHTML = 'Read our latest on Instagram <span>↗</span>';
}

document.querySelector('.menu-button').addEventListener('click', function () {
  document.querySelector('.main-nav').classList.toggle('is-open');
});
