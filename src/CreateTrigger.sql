CREATE TRIGGER FST_En_song AFTER INSERT ON song
  BEGIN
    INSERT INTO English_FTS (id,name,table_type) VALUES (new.id, new.name, 'S');
  end;

CREATE TRIGGER FST_En_album AFTER INSERT ON album
  BEGIN
    INSERT INTO English_FTS (id,name,table_type) VALUES (new.id,new.name,'A');
  end;

CREATE TRIGGER FST_En_credit AFTER INSERT ON credit
  BEGIN
    INSERT INTO English_FTS (id,name,table_type) VALUES (new.id,new.name,'C');
  end;