# Workaround for compilation error in meta-openembedded

CFLAGS += "-I=/usr/include/${PYTHON_DIR}${PYTHON_ABI}"
CXXFLAGS += "-I=/usr/include/${PYTHON_DIR}${PYTHON_ABI}"

