# MySQL & Postgres

[DBML](https://www.dbml.org/) comes with a [built-in CLI](https://www.dbml.org/cli) which can be used to convert between different formats from the command line.
We can use it in par with FloorPlan to generate ER diagrams for both [MySQL](https://www.mysql.com/) and [Postgres](https://www.postgresql.org/) databases.

First, we must translate our `.sql` files into `.dbml` files by using the CLI tools.

Install DBML's CLI with:

```
npm install -g @dbml/cli
```

And convert your schema dump:

```
# for a Postgres DB:
dbml2sql schema.dbml -o schema.sql

# or for MySQL
sql2dbml --mysql dump.sql -o mydatabase.dbml
```

The `.dbml` output can be [passed into `FloorPlan`](https://julioz.github.io/FloorPlan/run/) for the ER diagram generation.
