'use strict';
const fs = require('fs');
const transformer = require('metro-react-native-babel-transformer/src/index')

function customTransform(code, filename) {
  console.log("Generating sourcemap for " + filename);

  var map = fs.readFileSync(filename + '.map', {encoding: 'utf8'});
  var sourceMap = JSON.parse(map);
  var sourcesContent = [];

  sourceMap.sources.forEach(function(path) {
    var sourcePath = __dirname + '/' + path;

    if (fs.lstatSync(sourcePath).isDirectory()) {
      return
    }

    try {
      // try and find the corresponding `.cljs` file first
      sourcesContent.push(fs.readFileSync(sourcePath.replace('.js', '.cljs'), 'utf8'));
    } catch (e) {
      // otherwise fallback to whatever is listed as the source
      sourcesContent.push(fs.readFileSync(sourcePath, 'utf8'));
    }
  });

  sourceMap.sourcesContent = sourcesContent;

  return {
    filename: filename,
    code: code.replace("# sourceMappingURL=", ""),
    map: sourceMap
  };
}

exports.transform = function (data) {
  if (data.filename.match(/index\.(ios|android)\.js/)) {
    console.log('using custom transform for file:', data.filename);
    return customTransform(data.src, data.filename);
  } else {
    return transformer.transform(data);
  }
};
