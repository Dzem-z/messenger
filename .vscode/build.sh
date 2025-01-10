pwd
npm --prefix frontend run build
rm -r ./src/main/resources/static/*
cp -r ./frontend/build/* ./src/main/resources/static/