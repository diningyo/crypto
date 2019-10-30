// See LICENSE for license details.

package crypto.aes

import chisel3._
import chisel3.util._

/**
 * RotWord
 */
class RotWord extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(32.W))
    val out = Output(UInt(32.W))
  })

  val w_in_data = Range(32, 0, -8).map(i => io.in(i - 1, i - 8))
  val w_out_data = Range(0, 4).map {
    case i if i == 3 => w_in_data(0)
    case i => w_in_data(i + 1)
  }

  io.out := Cat(w_out_data)
}

object ElaborateRotWord extends App {
  Driver.execute(Array(
    "-td=rtl"
  ),
    () => new RotWord
  )
}