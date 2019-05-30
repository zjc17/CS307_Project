# 音乐数据库系统设计及主要操作介绍

## 数据库系统设计介绍

### 系统需求分析及定义

在这个个人音乐数据库系统中，最主要的功能是

- 批量解析音乐文件，获取音乐标签信息
- 根据路径播放音乐
- 编辑音乐文件信息
- 编辑专辑信息
- 编辑艺术家信息
- 实现歌单、音乐、专辑自定义排序
- 由于无法解析出多位艺术家，部分信息需要手动添加
- ……

> 所有的需求可以概括为对各个实体的增、删、查、改功能，具体的实现在下文介绍

### 数据需求分析及定义

> 由于主要面向个人资料的管理，并且不提供云音乐的播放，因此采用本地化更容易的`sqlite`作为数据库系统。
>
> 由于 `sqlite`中数据类型的特殊性，因此部分数据长度、类型等限制需要在客户端软件完成。

这里数据库内存在的实体：

- people
- song
- album
- picture (未来可能会更新为file, 通过file_type进行区分，主要保存音乐、图片文件的路径信息)
- playlist
- folder
- genre

通过一下表组织关系模型

- credit: 与`people`表相关，通过 `credit_as` 区分不同身份的艺术家
- credit_with_album, credit_with_song: 艺术家与专辑、歌曲的关系组织
- song_has_genre: 连结 `song` 与 `genre` 的表
- playlist_has_song: 连结 `song` 与 `playlist` 的表

> 关系模型可以极大地降低数据冗余

### 数据模型设计

![1559022102899.png][2]

### View

由于 `credit` 表的存在，使得查询歌手、作曲和填词人比较麻烦，因此创建view来加快操作。

```sql
BEGIN TRANSACTION;

DROP VIEW IF EXISTS "artist";
CREATE VIEW IF NOT EXISTS "artist" AS
SELECT p.id            AS id,
       p.name          AS name,
       p.name_for_sort AS name_for_sort,
       p.picture_id    AS picture_id,
       c.id            AS credit_id
FROM people p
       INNER JOIN credit c ON p.id = c.people_id
WHERE credit_as = 'A';

DROP VIEW IF EXISTS "album_artist";
CREATE VIEW IF NOT EXISTS "album_artist" AS
SELECT credit.id            AS id,
       people.name          AS name,
       credit.name_for_sort AS name_for_sort,
       people.picture_id    AS picture_id
FROM people
       INNER JOIN credit ON people.id = credit.people_id
WHERE credit_as = 'M';

DROP VIEW IF EXISTS "composer";
CREATE VIEW IF NOT EXISTS "composer" AS
SELECT credit.id            AS id,
       people.name          AS name,
       credit.name_for_sort AS name_for_sort,
       people.picture_id    AS picture_id
FROM people
       INNER JOIN credit ON people.id = credit.people_id
WHERE credit_as = 'C';

COMMIT;
```

## SQL介绍

### Insert

1. 添加新歌曲

```sql
INSERT OR IGNORE INTO song 
(name, file_path, picture_id, album_id, year, bpm, sampleRate, bitRate, mpeg_version, mpeg_layer, channels, comments, size, length, track_order) 
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
```

2. 添加新图片


```sql
INSERT OR IGNORE INTO picture 
(path) 
VALUES (?)
```

3. 添加新曲风

```sql
INSERT OR IGNORE INTO song_has_genre 
(song_id, genre_id, "order") 
VALUES (?, ?, ?)

INSERT INTO song_has_genre 
(song_id, genre_id, "order")
VALUES (?, ?, (SELECT count(song_id) cnt 
               FROM song_has_genre 
               WHERE song_id = ?))
```

4. 添加一首歌（的多个）艺术家

```sql
INSERT OR IGNORE INTO credit_with_song 
(credit_id, song_id, "order") 
VALUES (?, ?, ?)

INSERT INTO credit_with_song 
(credit_id, song_id, "order")
VALUES (?, ?, (SELECT count(song_id) cnt 
               FROM credit_with_song 
               WHERE song_id = ?))
```

5. 添加一份专辑（的多个）艺术家

```sql
INSERT OR IGNORE INTO credit_with_album 
(credit_id, album_id, "order") 
VALUES (?, ?, ?)

INSERT INTO credit_with_album 
(credit_id, album_id, "order")
VALUES (?, ?, (SELECT count(album_id) cnt 
               FROM credit_with_album 
               WHERE album_id = ?))
```

6. 添加新的艺术家类型

```sql
INSERT OR IGNORE INTO credit 
(people_id, credit_as) 
VALUES (?, ?)
```

7. 添加新的艺术家

```sql
INSERT INTO people 
(name, picture_id) 
VALUES (?, ?)
```

8. 添加新的曲目风格

```sql
INSERT OR IGNORE INTO genre 
(name) 
VALUES (?)
```

9. 添加新的专辑

```sql
INSERT OR IGNORE INTO album 
(name, picture_id, track_total_number) 
VALUES (?, ?, ?)
```

10. 添加新的歌单

```sql
INSERT INTO playlist 
(name, folder_id) 
VALUES (?, 1)
```

11. 向已有歌单中添加已有歌曲

```sql
INSERT INTO playlist_has_song 
(playlist_id, song_id, "order")
VALUES (?, ?, (SELECT count(playlist_id) cnt 
               FROM playlist_has_song 
               WHERE playlist_id = ?))
—— 这里可以使用trigger优化操作
```

12. 为歌曲增加新的风格*

```sql
INSERT INTO song_has_genre (song_id, genre_id) VALUES (?, ?)
```

### Update

1. 对歌曲进行评分

```sql
UPDATE song 
    SET rating = 2, 
        date_modified = (datetime(CURRENT_TIMESTAMP, 'localtime')) 
WHERE id = 1
```

2. 修改歌曲排序用的名字*

```sql
UPDATE song 
    SET name_for_sort = ?,
    date_modified = (datetime(CURRENT_TIMESTAMP, 'localtime')) 
WHERE id = ?
```

3. 对专辑进行评分*

```sql
UPDATE album SET rating = ? WHERE id = ?
```

4. 修改专辑排序用的名字*

```sql
UPDATE album SET name_for_sort = ? WHERE id = ?
```

5. 修改艺术家排序用的名字*

```sql
UPDATE people SET name_for_sort = ? WHERE id = ?
```

### DELETE

1. 删除歌单

```sql
DELETE FROM playlist WHERE id = ?
```

2. 从歌单中删除歌曲

```sql
DELETE FROM playlist_has_song WHERE song_id = ? AND playlist_id = ?
```

3. 删除歌曲*

```sql
DELETE FROM song WHERE id = ?
```


> 同时执行删除文件

4. 删除歌曲现有风格

```sql
DELETE FROM song_has_genre WHERE song_id = ?
```

### SELETE

#### 辅助插入歌曲查询

1. 查询歌曲

```sql
SELECT id FROM song WHERE name = ?
```

2. 查询艺术家credit_id

```sql
SELECT id FROM credit WHERE people_id = ? AND credit_as = ?

SELECT c.id 
FROM credit c 
WHERE credit_as = ? 
AND people_id = (SELECT p.id
                 FROM people p
                 WHERE name = ?)
```

3. 查询艺术家

```sql
SELECT id FROM people WHERE name = ?
```


4. 查询曲风编号

```sql
SELECT id FROM genre WHERE name = ?
```

5. 查询专辑

```sql
SELECT id FROM album WHERE name = ?
```

#### 详细信息查询

1. 专辑

```sqlite
SELECT id, name, picture_id
FROM album
ORDER BY CASE
           WHEN name_for_sort IS NOT NULL
             THEN name_for_sort
           ELSE name END
LIMIT 5 offset ?
```

![1559021447704.png][3]

2. 歌曲

```sql
SELECT s.id,
       s.name,
       s.length,
       CAST(((s.length / 60) || ':' || (s.length % 60)) AS TEXT) AS length_in_minute,
       a.name                                                    AS album_name,
       g.name                                                    AS genre_name,
       s.rating,
       s.file_path,
       s.picture_id
FROM song s
       JOIN (SELECT name, id FROM album) a ON a.id = s.album_id
       JOIN (SELECT song_id, genre_id, "order" AS genre_id FROM song_has_genre) sg ON s.id = song_id
       JOIN (SELECT name, id FROM genre) g ON g.id = sg.genre_id
ORDER BY CASE
           WHEN s.name_for_sort IS NOT NULL
             THEN s.name_for_sort
           ELSE s.name
           END
LIMIT 10 OFFSET ?;
```

![1559021511354.png][4]

目录管理

```sql
-- File.separator
SELECT artist.name AS artist_name,
       a.name AS album_name,
       s.name AS song_name,
       artist.name || '\\' || a.name || '\\' || s.name || '.mp3' AS path
FROM artist
  INNER JOIN credit_with_song
  INNER JOIN song s on credit_with_song.song_id = s.id
  INNER JOIN album a on s.album_id = a.id;
```

![1559029819440.png][5]

1. 播放列表

```sql
SELECT id, name FROM playlist
```

![1559021597368.png][6]

```sql
WITH songInPlaylist AS (SELECT playlist_id, song_id, "order" order_no
                        FROM playlist_has_song
                        WHERE playlist_id = ?
                        ORDER BY order_no)
SELECT s.id,
       s.name,
       s.length,
       CAST(((s.length / 60) || ':' || (s.length % 60)) AS TEXT) AS length_in_minute,
       a.name                                                    AS album_name,
       g.name                                                    AS genre_name,
       s.rating,
       s.file_path,
       s.picture_id
FROM songInPlaylist sp
       INNER JOIN song s ON sp.song_id = s.id
       INNER JOIN (SELECT name, id FROM album) a ON a.id = s.album_id
       INNER JOIN (SELECT sg.song_id, genre_id, "order" AS genre_id FROM song_has_genre sg) sg
            ON s.id = sg.song_id
       INNER JOIN (SELECT name, id FROM genre) g ON g.id = sg.genre_id
ORDER BY sp.order_no
LIMIT 10 OFFSET ?;
```

![1559021832828.png][7]

自定义快速生成歌单

- 评分范围
- 添加日期范围
- 修改日期范围
- 歌曲风格
- 歌曲时长

```sql
SELECT s.id,
       s.name,
       s.length,
       CAST(((s.length / 60) || ':' || (s.length % 60)) AS TEXT) AS length_in_minute,
       a.name                                                    AS album_name,
       g.name                                                    AS genre_name,
       s.rating,
       s.file_path,
       s.picture_id
FROM song s
        INNER JOIN (SELECT name, id FROM album) a ON a.id = s.album_id
        INNER JOIN (SELECT song_id, genre_id, "order" AS genre_id FROM song_has_genre) sg ON s.id = song_id
        INNER JOIN (SELECT name, id FROM genre) g ON g.id = sg.genre_id
        WHERE rating BETWEEN 0 AND 1
          AND date_added BETWEEN '2019-05-28 13:52:43' AND '2019-05-28 13:52:43'
          AND date_modified BETWEEN '2019-05-28 13:52:43' AND '2019-05-28 13:52:43'
          AND genre_name IN ('Blues')
          AND length BETWEEN 200 AND 210
ORDER BY CASE
           WHEN s.name_for_sort IS NOT NULL
             THEN s.name_for_sort
           ELSE s.name
           END;
```

![1559029127247.png][8]

4. 艺术家

```sql
SELECT id, name
FROM artist a
ORDER BY CASE
           WHEN a.name_for_sort IS NOT NULL
             THEN a.name_for_sort
           ELSE a.name
           END
LIMIT 5 OFFSET ?;

SELECT id, name
FROM composer a
ORDER BY CASE
           WHEN a.name_for_sort IS NOT NULL
             THEN a.name_for_sort
           ELSE a.name
           END
LIMIT 5 OFFSET ?;
```

![1559021981599.png][9]

5. 播放路径

```sql
SELECT file_path FROM song WHERE id = 56;
```

6. 查询某个艺术家的歌曲

```sql
WITH singer AS (SELECT * FROM artist WHERE id = ?)
SELECT song_id, s.name AS song_name
FROM singer
       INNER JOIN credit_with_song ON singer.credit_id = credit_with_song.credit_id
       INNER JOIN song s on credit_with_song.song_id = s.id
LIMIT 10 OFFSET ?
```

![1559038498270.png][10]

7. 查询某个专辑的歌曲

```sql
SELECT id, name, file_path FROM song WHERE album_id = 2
```

![1559038522815.png][11]



## 重要模块

- `DB_Connector`
  - 连接数据库
  - 设置是否自动提交
  - 断开连接
- `DB_Reader`
  - 执行所有的查找命令
  - 使用单独的连接，未来可以与数据库写入进行多线程操作
- `DB_Writer`
  - 执行所有的写入操作
  - 使用单独的连接，未来可以与数据库读取进行多线程操作
- `Menu_*`: 与用户进行交互的各个菜单
- `File_add`、`Parser_*`：对音乐文件的批量解析

### 优化

1. 批量读取优化

> 通过设置 `batch_size`， 使得在批量读取时不必每次读取都向数据库提交更改，从而大大提交效率。
>
> 实际测试中，在批量添加87首歌曲的情况下，未使用 `batch_size` 耗时约 35s， 使用后耗时仅 1.571秒
>
> （仅进行了一次测试，但足以说明效率的提升）

```java
private void getFileName(String dirPath) {
    connector.setAutoCommit(false);   // 35 - 1.571
    File file = new File(dirPath);    //获取其file对象
    func(file);
    connector.setAutoCommit(true);
}
private void func(File file) {
    File[] fs = file.listFiles();
    int cnt = 0;
    for (File f : fs) {
      if (f.isDirectory()) {
        func(f);
      }
      if (f.isFile()) {
        String fileName = f.getName();
        String filePath = f.getPath();
        if (filePath.endsWith("mp3")) {
          System.out.println(fileName);
          parseMP3(filePath);
          cnt++;
        }
      }
      if (cnt == batch_size) {
        connector.commit();
        cnt = 0;
      }

    }
    if (cnt > 0) {
      connector.commit();
    }
  }
```

2. 添加音乐时的优化

> 这里仅展示其中一份代码，原理相同

​   对于类似 `song` 这样存在外键约束的实体，需要先获取外键数据，这里主要有一下几种方案：

- 使用 `INSERT OR IGNORE`， 之后执行 `SELECT`

- 首先执行`SELECT`，判断是否有结果，若无则执行`INSERT`，再执行`SELECT`

  这里虽然方案一看起来操作少，实际测试中发现，在数据已经存在的情况下， `INSERT OR IGNORE` 的耗时较大。

  并且考虑到艺术家信息大概率已经存在于数据库中，并且可能存在多次读取同一个资料库的情况，

  因此采用方案二优化耗时。

  （此处由于需要改动的代码较多，因此未做实际的测试比较，仅通过控制台比较了多次单个方案一和二的耗时）

3. 搜索功能优化

> 用户在搜索时可能出现无法记住准确全名的情况，因此需要进行模糊搜素；为了模糊搜索的速度，使用全文索引来提升速度：

```sql
CREATE VIRTUAL TABLE English_FTS USING fts5(id,name,table_type);
 
CREATE TRIGGER FST_En_song AFTER INSERT ON song
 BEGIN
 INSERT INTO English_FTS (id,name,table_type) VALUES (new.id,new.name,'S');
 END;
 
CREATE TRIGGER FST_En_album AFTER INSERT ON album
  BEGIN
  INSERT INTO English_FTS (id,name,table_type) VALUES (new.id,new.name,'A');
  END;
  
CREATE TRIGGER FST_En_credit AFTER INSERT ON people
    BEGIN
    INSERT INTO English_FTS (id,name,table_type) VALUES (new.id,new.name,'P');
    END;
```

> 虚表的创建是为了全文索引服务，与整体的表结构无关；
>
> trigger是为了在每次insert的时候将元组的id以及用于搜索的name加入表English_FTS，以便搜索使用；

虚表中搜索：

```sql
WITH sel_song AS
    (SELECT * FROM song
               LEFT JOIN (SELECT id
                     FROM English_FTS
                     WHERE (table_type = 'S')
                     AND (name match ?)
    )
)
SELECT * -- 这里的属性与前面选择歌曲相同，为了控制篇幅使用*代替
FROM sel_song
       left JOIN credit_song ON sel_song.id = credit_song.song_id
       left JOIN album ON sel_song.album_id = album.id
       left JOIN picture ON sel_song.picture_id = picture.id
where (credit_as = 'A');
```

> 但是由于FTS标准中缺乏中文分词器，而大部分中文分词器（如微信使用的`mmicu`，`icu`等）的导入在java环境下非常困难，中文的搜索只好使用

```sql
SELECT FROM song WHERE name LIKE ?
```

## 部分操作演示

1. 搜索

![search.gif][12]

2. 播放列表

![playlist.gif][13]

3. 艺术家

![artist2.gif][14]

4. 播放演示

[vplayer url="https:\/\/www.zhangjc.site\/usr\/uploads\/2019\/05\/1647886151.mp4"  /]


  [2]: https://www.zhangjc.site/usr/uploads/2019/05/2380519652.png
  [3]: https://www.zhangjc.site/usr/uploads/2019/05/999464255.png
  [4]: https://www.zhangjc.site/usr/uploads/2019/05/351650243.png
  [5]: https://www.zhangjc.site/usr/uploads/2019/05/1740332704.png
  [6]: https://www.zhangjc.site/usr/uploads/2019/05/2405495203.png
  [7]: https://www.zhangjc.site/usr/uploads/2019/05/2169527323.png
  [8]: https://www.zhangjc.site/usr/uploads/2019/05/3888264737.png
  [9]: https://www.zhangjc.site/usr/uploads/2019/05/3015841524.png
  [10]: https://www.zhangjc.site/usr/uploads/2019/05/31223254.png
  [11]: https://www.zhangjc.site/usr/uploads/2019/05/1548072447.png
  [12]: https://www.zhangjc.site/usr/uploads/2019/05/2422184015.gif
  [13]: https://www.zhangjc.site/usr/uploads/2019/05/1918235711.gif
  [14]: https://www.zhangjc.site/usr/uploads/2019/05/3201708632.gif
  [15]: https://www.zhangjc.site/usr/uploads/2019/05/1647886151.mp4
