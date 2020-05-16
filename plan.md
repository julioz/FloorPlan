Convert the output of FloorPlan (DBML) to GraphViz () input format, a .gv file.
Once with that, we can generate an SVG for the ER diagram with
```
dot input.gv -Tsvg -o out.svg
```
And plot it into a mkdocs or another markdown viewer (like github).

Source blog: https://spin.atomicobject.com/2017/11/15/table-rel-diagrams-graphviz/
