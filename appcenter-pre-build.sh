#!/usr/bin/env bash

brew install leiningen

case $TARGET_PLATFORM in
    ios)
        lein do clean, with-profile prod cljsbuild once ios
        exit
        ;;
    android)
        lein do clean, with-profile prod cljsbuild once android
        echo
        exit
        ;;
    *)
        echo "Only ios and android target platforms are supported"
        exit 1
esac
