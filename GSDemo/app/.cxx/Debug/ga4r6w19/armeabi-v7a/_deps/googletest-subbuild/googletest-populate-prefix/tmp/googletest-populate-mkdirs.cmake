# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file Copyright.txt or https://cmake.org/licensing for details.

cmake_minimum_required(VERSION 3.5)

file(MAKE_DIRECTORY
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-src"
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-build"
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix"
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix/tmp"
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix/src/googletest-populate-stamp"
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix/src"
  "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix/src/googletest-populate-stamp"
)

set(configSubDirs )
foreach(subDir IN LISTS configSubDirs)
    file(MAKE_DIRECTORY "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix/src/googletest-populate-stamp/${subDir}")
endforeach()
if(cfgdir)
  file(MAKE_DIRECTORY "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/ga4r6w19/armeabi-v7a/_deps/googletest-subbuild/googletest-populate-prefix/src/googletest-populate-stamp${cfgdir}") # cfgdir has leading slash
endif()
