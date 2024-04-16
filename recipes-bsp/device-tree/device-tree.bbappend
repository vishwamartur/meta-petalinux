FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SDFEC_EXTRA_OVERLAYS ??= ""
SDFEC_EXTRA_OVERLAYS:zcu111-zynqmp = "system-zcu111.dtsi"

EXTRA_OVERLAYS:append = "${@' ${SDFEC_EXTRA_OVERLAYS}' if d.getVar('ENABLE_SDFEC_DT') == '1' else ''}"
