// See LICENSE for license details.

package crypto.aes

import chisel3._
import test.util.{BaseSimDTM, BaseSimDTMIO}

class SimDTMAESTop()(limit: Int, abortEn: Boolean = true) extends BaseSimDTM(limit, abortEn) {
  val io = IO(new Bundle with BaseSimDTMIO {
    val aes = new AESTopIO
  })

  val m_dut = Module(new AESTop)

  io.aes <> m_dut.io

  io.finish := false.B
  io.timeout := false.B
}
