const { chromium } = require('playwright');
const fs = require('fs');

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage();
  await page.goto('https://lovable.dev/preview/0TysiMgBdsSIc3aBMBtkpAsUxUqo4KOd', { waitUntil: 'networkidle' });
  const html = await page.content();
  fs.writeFileSync('lovable.html', html);
  
  // also grab any iframes
  const frames = page.frames();
  for (let i = 0; i < frames.length; i++) {
    try {
        const frameHtml = await frames[i].content();
        fs.writeFileSync('lovable_frame_' + i + '.html', frameHtml);
    } catch(e) {}
  }
  
  await browser.close();
})();
