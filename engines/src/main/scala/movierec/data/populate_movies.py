#!/usr/bin/env python

# Import the imdb package.
import imdb
import imdb.helpers

def code(s):
	return s.encode('ascii', 'replace')

# Create the object that will be used to access the IMDb's database.
ia = imdb.IMDb() # by default access the web.

movieList = open('u.item', 'r')
updatedMovieList = open('u.item.extended', 'w')
newFeatureList = ['director', 'cast', 'writer', 'runtimes', 'countries', 'languages']

for line in movieList:
	features = line.rstrip().split('|')
	done = False
	count = 0

	try:
		imdbID = ia.title2imdbID(unicode(features[1], 'iso-8859-1'))

		movie_result = ia.get_movie(imdbID)
	except Exception, e:
		while done == False:
			try:
				results = ia.search_movie(unicode(features[1], 'iso-8859-1'))
				movie_result = results[0]
				done = True
			except Exception, e:	
				count += 1
				print features[1]
				print e
				if (count == 3):
					done = True

	done = False
	count = 0
	ia.update(movie_result)

	while done == False:
		try:
			directors = ",".join(code(director['name']) for director in movie_result['director'])
			features.append(directors)

			writers = ",".join(code(writer['name']) for writer in movie_result['writer'])
			features.append(writers)

			actors = ",".join(code(actor['name']) for actor in movie_result['cast'])
			features.append(actors)

			features.append(code(movie_result['runtimes'][0]))

			countries = ",".join(code(country) for country in movie_result['countries'])
			features.append(countries)

			languages = ",".join(code(language) for language in movie_result['languages'])
			features.append(languages)

			certificates = ",".join(code(certificate) for certificate in movie_result['certificates'])
			features.append(certificates)

			features.append(code(movie_result['plot'][0]))
			done = True
		except Exception, e:
			count += 1
			print features[1]
			print e
			if (count == 3):
				done = True

	updatedMovieList.write('|'.join(features) + '\n')