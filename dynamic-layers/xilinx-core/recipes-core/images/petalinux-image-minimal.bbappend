#To support yocto native zcu104 SDT BSP, adding the rootfs packages for petalinux distro 
IMAGE_INSTALL:append:zynqmp-zcu104-sdt-full = "\
	packagegroup-xilinx-gstreamer \
	packagegroup-xilinx-matchbox \
	packagegroup-core-x11 \
	libdrm \
	libdrm-tests \
	gstreamer-vcu-examples \
	v4l-utils \
	yavta \
	packagegroup-xilinx-audio \
	libmali-xlnx \
	zocl \
	xrt \
	opencl-headers \
	gstreamer-vcu-notebooks \
	vcu-ctrlsw \
	"

