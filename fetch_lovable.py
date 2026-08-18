import urllib.request
import json
import re

url = "https://lovable.dev/preview/0TysiMgBdsSIc3aBMBtkpAsUxUqo4KOd"
req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
try:
    html = urllib.request.urlopen(req).read().decode('utf-8')
    # search for project data or iframe
    match = re.search(r'__INITIAL_STATE__\s*=\s*({.*?});', html)
    if match:
        print("Found initial state")
    else:
        print("Initial state not found")
        # Try to find iframe
        iframes = re.findall(r'<iframe[^>]*src="([^"]+)"', html)
        print("Iframes:", iframes)
        
        # Try to extract window.__PRELOADED_STATE__
        match = re.search(r'window\.__PRELOADED_STATE__\s*=\s*({.*?});', html)
        if match:
            print("Found preloaded state")
        
        # Look for any json in script tags
        print("Looking for supabase or project urls...")
        urls = re.findall(r'https://[^"]+\.supabase\.co[^"]*', html)
        print("Supabase URLs:", urls)
        
        urls = re.findall(r'https://[^"]*lovable[^"]*', html)
        print("Lovable URLs:", urls[:5])
        
except Exception as e:
    print(e)
