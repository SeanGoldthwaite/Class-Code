from bs4 import BeautifulSoup

with open('countries.txt','r') as f:
    html = '\n'.join(f.readlines())

soup = BeautifulSoup(html, 'html.parser')

lis = soup.find_all('li')
articles = []

for li in lis:
    articles.append(li.a.string)

articles = list(map(lambda x : str(x).replace('\xa0',' '),articles))

# print(', '.join(articles))
print(articles)