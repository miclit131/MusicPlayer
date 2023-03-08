'use strict';

const gulp = require('gulp');
const sass = require('gulp-sass');
const clean = require('gulp-clean');

const pathRes = './src/main/resources/';

gulp.task('sass', function () {
    try {
        gulp.src('./target/', {read: false})
            .pipe(clean());
    } catch (e) {
        console.error("target/ folder missing");
    }
    return gulp.src(pathRes + 'sass/themes/*.sass')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest(pathRes + 'css/'));
});

gulp.task('watch', function () {
    gulp.watch(pathRes + 'sass/**/*.sass', gulp.series('sass'));
});