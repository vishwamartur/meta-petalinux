# Both arm and armv*r overrides are set w/ cortex r5
# So only set rmprofile if armv*r is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE:armv7r = "rmprofile"
ARM_PROFILE:armv8r = "rmprofile"
EXTRA_OECONF:append:xilinx-standalone:arm:baremetal-multilib-tc = " \
        --with-multilib-list=${ARM_PROFILE} \
        "

# RISC V configuration
# Upstream default multilibs:
#RISCV_MULTILIB = "rv32i-ilp32--c;rv32im-ilp32--c;rv32iac-ilp32--;rv32imac-ilp32--;rv32imafc-ilp32f-rv32imafdc-;rv64imac-lp64--;rv64imafdc-lp64d--"
# Original request:
RISCV_MULTILIB = "rv32i-ilp32--c;rv32im-ilp32--c;rv32imc-ilp32--;rv32iac-ilp32--;rv32imac-ilp32--;rv32imafc-ilp32f--;rv32imafdc-ilp32d--;rv64i-lp64--;rv64im-lp64--;rv64imac-lp64--;rv64imafdc-lp64d--"
# Additional request libraries:
RISCV_MULTILIB .= ";rv32imac_zicsr_zifencei-ilp32--;rv64imac_zicsr_zifencei-lp64--"
EXTRA_OECONF:append:xilinx-standalone:riscv32:baremetal-multilib-tc = " \
	--with-multilib-generator='${RISCV_MULTILIB}' \
	"
EXTRA_OECONF:append:xilinx-standalone:riscv64:baremetal-multilib-tc = " \
	--with-multilib-generator='${RISCV_MULTILIB}' \
	"
