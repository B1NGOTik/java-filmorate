# Схема базы данных для приложения java-filmorate
![Схема базы данных](https://github.com/B1NGOTik/java-filmorate-db/blob/master/java-filmorate-db.png)
## Пояснение к схеме
### Связи 
* Поле 'user_id' таблицы 'users' имеет связь один-ко-многим с полем 'user_id' таблицы 'friendships'

* Поле 'user_id' таблицы 'users' имеет связь один-ко-многим с полем 'friend_id' таблицы 'friendships'

* Поле 'user_id' таблицы 'users' имеет связь один-ко-многим с полем 'user_id' таблицы 'film_likes'

* Поле 'film_id' таблицы 'film' имеет связь один-ко-многим с полем 'film_id' таблицы 'film_likes'

* Поле 'rating_id' таблицы 'film_ratings' имеет связь один-ко-многим с полем 'rating_id' таблицы 'film'

* Поле 'film_id' таблицы 'film' имеет связь один-ко-многим с полем 'film_id' таблицы 'film_genres'

* Поле 'genre_id' таблицы 'genres' имеет связь один-ко-многим с полем 'genre_id' таблицы 'film_genres'
