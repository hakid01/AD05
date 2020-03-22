# AD05

## Crear tablas de base datos minidrive

```sql
create table directorios 
(id serial primary key, 
nome text not null unique);

create table arquivos 
(id serial, nome text not null, 
id_directorio integer not null references directorios(id), 
arquivo bytea not null, 
primary key (nome, id_directorio));
```

## Datos de conexión db y path local
Los datos de conexión a la base de datos y el path donde se guardan los archivos en local están en un archico llamado data.json, situado dentro del proyecto en src/main/resources/data.json

Modificar en este archivo el path existente por uno válido en la máquina donde se ejecute el programa.

## Funcionamiento app minidrive
Al ejecutar se abre un frame principal con dos botones, iniciar y detener.
