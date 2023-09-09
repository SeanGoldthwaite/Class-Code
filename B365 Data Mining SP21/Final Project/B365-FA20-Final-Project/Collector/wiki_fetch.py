import wikipedia
import csv, re, os, codecs, time

def wikipedia_pages_parse_csv(csvf):
    articles = []
    with open(csvf,'r',encoding ='latin1') as csvf:
        reader = csv.reader(csvf)
        for row in reader:
            try:
                int(row[0])
                articles.append(row[1])
            except:
                pass
    return articles

def create_file(path,lang,title,article):
    with codecs.open(os.path.join(path,lang,title) + '.txt','w',"utf-8-sig") as f:
        f.write(article)

def decite(article):
    return re.sub("\[[0-9]\]|\[[0-9][0-9]\]|==+","",article)

def mkdir_if_not_exists(dir):
    if not os.path.exists(dir):
        os.makedirs(dir)

def run(languages,topics,path=None):
    page_errors = 0
    disambig_errors = 0
    articles_produced = 0
    start = time.perf_counter()

    path = path if path else os.path.join('Parser','data')
    
    for lang in languages:
        wikipedia.set_lang(lang)
        mkdir_if_not_exists(os.path.join(path,lang))
        print("|| Language:",lang)
        for topic in topics:
            try:
                article = wikipedia.page(topic)
                content = article.content
                decite(content)
                create_file(path,lang,topic,content)
                articles_produced += 1
                print(topic)
            except wikipedia.exceptions.PageError as e:
                page_errors += 1
                print(f"~~ PAGE ERROR ~~  {topic}")
            except wikipedia.exceptions.DisambiguationError as e:
                disambig_errors += 1
                print(f"~~ DISAMBIGUATION ERROR ~~  {topic}")
    end = time.perf_counter()
    total_seconds = end - start
    minutes = int(total_seconds // 60)
    seconds = int(total_seconds % 60)
    print(f"Articles produced: {articles_produced}\nPage errors: {page_errors}\nDisambiguation errors: {disambig_errors}\n\nIn {minutes}m {seconds}s")


if __name__ == "__main__":
    training = input("Override training? y/n: ").lower()
    testing = input("Override testing data? y/n: ").lower()
    if training == 'y':
        run(['en','de','fr','it','es'],wikipedia_pages_parse_csv(os.path.join('Collector','wikipedia_pages.csv')))
    if testing == 'y':
        chemistry = ['Acid–base chemistry', 'Analytical chemistry', 'Astrochemistry', 'Biochemistry', 'Colloidal chemistry', 'Crystallography', 'Chemical engineering', 'Environmental chemistry', 'Food science', 'Geochemistry', 'Green chemistry', 'Inorganic chemistry', 'Materials science', 'Medicinal chemistry', 'Metallurgy', 'Molecular physics', 'Nuclear chemistry', 'Organic chemistry', 'Organometallic chemistry', 'Photochemistry', 'Physical chemistry', 'Radiochemistry', 'Solid-state chemistry', 'Stereochemistry', 'Supramolecular chemistry', 'Surface chemistry', 'Theoretical chemistry']
        print("\n\nCHEMISTRY\n\n")
        run(['en','de','fr','it','es'],chemistry,os.path.join('Parser','chemistry_data'))
        countries = ['Afghanistan', 'Andorra', 'Argentina', 'Australia', 'Austria', 'Azerbaijan', 'Bahrain', 'Bangladesh', 'Belarus', 'Belgium', 'Belize', 'Bolivia', 'Bosnia and Herzegovina', 'Brazil', 'Bulgaria', 'Burundi', 'Cambodia', 'Cameroon', 'Canada', 'Central African Republic', 'Chile', 'China', 'Colombia', 'Costa Rica', 'Croatia', 'Cuba', 'Cyprus', 'Czech Republic', 'Democratic Republic of the Congo', 'Denmark', 'Dominican Republic', 'Ecuador', 'Egypt', 'El Salvador', 'Eritrea', 'Estonia', 'Finland', 'France', 'The Gambia', 'Georgia', 'Germany', 'RhÃ¶n', 'Gibraltar', 'Greece', 'Guatemala', 'Guinea', 'Guyana', 'Haiti', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran', 'Iraq', 'Ireland', 'Israel', 'Italy', 'Jamaica', 'Japan', 'Jordan', 'Kenya', 'Kurdistan', 'Kuwait', 'Laos', 'Latvia', 'Lebanon', 'Liberia', 'Libya', 'Lithuania', 'Luxembourg', 'Madagascar', 'Malawi', 'Malaysia', 'Mali', 'Malta', 'Mauritania', 'Mexico', 'Moldova', 'Monaco', 'Morocco', 'Nepal', 'Netherlands', 'New Zealand', 'Nicaragua', 'Nigeria', 'North Korea', 'North Macedonia', 'Norway', 'Oman', 'Pakistan', 'Palestine', 'Panama', 'Paraguay', 'Peru', 'Philippines', 'Poland', 'Portugal', 'Puerto Rico', 'Republic of the Congo', 'Romania', 'Russia', 'SÃ£o TomÃ© and PrÃ\xadncipe', 'Saudi Arabia', 'Senegal', 'Serbia', 'Sierra Leone', 'Singapore', 'Slovakia', 'Slovenia', 'Somalia', 'South Africa', 'South Korea', 'Spain', 'Sri Lanka', 'Suriname', 'Sweden', 'Switzerland', 'Taiwan', 'Tajikistan', 'Tanzania', 'Thailand', 'Trinidad and Tobago', 'Turkey', 'Turkmenistan', 'Tuvalu', 'Uganda', 'Ukraine', 'United Arab Emirates', 'United Kingdom', 'Northern Ireland', 'Scotland', 'Wales', 'United States', 'Uruguay', 'Uzbekistan', 'Vatican City', 'Venezuela', 'Vietnam', 'Western Sahara', 'Zambia']
        print("\n\nCOUNTRIES\n\n")
        run(['en','de','fr','it','es'],countries,os.path.join('Parser','country_data'))
        food = ['asparagus', 'apples', 'avacado', 'alfalfa', 'almond', 'arugala', 'artichoke', 'applesauce', 'antelope', 'albacore tuna', 'apple juice', 'avocado roll', 'bruscetta', 'bacon', 'black beans', 'bagels', 'baked beans', 'bbq', 'bison', 'barley', 'beer', 'bisque', 'bluefish', 'bread', 'broccoli', 'buritto', 'babaganoosh', 'cabbage', 'cake', 'carrots', 'carne asada', 'celery', 'cheese', 'chicken', 'catfish', 'chips', 'chocolate', 'chowder', 'clams', 'coffee', 'cookies', 'corn', 'cupcakes', 'crab', 'curry', 'cereal', 'chimichanga', 'dates', 'dips', 'duck', 'dumplings', 'donuts', 'eggs', 'enchilada', 'eggrolls', 'english muffins', 'edimame', 'eel sushi', 'fajita', 'falafel', 'fish', 'franks', 'fondu', 'french toast', 'french dip', 'garlic', 'ginger', 'gnocchi', 'goose', 'granola', 'grapes', 'green beans', 'guancamole', 'gumbo', 'grits', 'graham crackers', 'ham', 'halibut', 'hamburger', 'honey', 'huenos rancheros', 'hash browns', 'hot dog', 'hummus', 'ice cream', 'irish stew', 'indian food', 'italian bread', 'jambalaya', 'jelly', 'jerky', 'jalapeã±o', 'kale', 'kabobs', 'ketchup', 'kiwi', 'kidney beans', 'kingfish', 'lobster', 'lamb', 'linguine', 'lasagna', 'meatballs', 'moose', 'milk', 'milkshake', 'noodles', 'ostrich', 'pizza', 'pepperoni', 'porter', 'pancakes', 'quesadilla', 'quiche', 'reuben', 'spinach', 'spaghetti', 'tater tots', 'toast', 'venison', 'waffles', 'wine', 'walnuts', 'yogurt', 'ziti', 'zucchini']
        print("\n\nFOOD\n\n")
        run(['en','de','fr','it','es'],food,os.path.join('Parser','food_data'))