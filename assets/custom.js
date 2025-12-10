window.onload = function () {
  const menu = document.querySelector('.site-menu');
  if (menu) {
    // eslint-disable-next-line no-undef
    const mutation = new MutationObserver(function (list, observer) {
      const summaryTarget = 'summary.tsd-accordion-summary[data-key="Package"]';
      const summary = document.querySelector(summaryTarget);
      if (summary) {
        const summaryParent = summary.parentElement;
        if (summaryParent && summaryParent.tagName === 'DETAILS') {
          summaryParent.setAttribute('open', '');
          observer.disconnect();
        }
      }
    });

    mutation.observe(menu, {childList: true, subtree: true});
  }
};
