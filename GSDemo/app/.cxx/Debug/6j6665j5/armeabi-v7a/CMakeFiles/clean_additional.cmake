# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles/isoObject_wrap.dir/isoObjectJAVA_wrap.cxx"
  "isoObject_wrap.java"
  "isoObject_wrapJNI.java"
  )
endif()
