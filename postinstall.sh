#!/usr/bin/env bash

case $TARGET_PLATFORM in
    ios)
        brew install leiningen
        lein do clean, with-profile prod cljsbuild once ios
        exit
        ;;
    android)
        brew install leiningen
        lein do clean, with-profile prod cljsbuild once android
        echo
        exit
        ;;
    *)
        echo "Only ios and android target platforms are supported"
        exit
esac
