// See LICENSE for license details.

package crypto.aes

import chisel3._
import chisel3.iotesters._

/**
 * Unit test class for AESTop
 * @param c Instance of AESTop.
 */
class AESTopUnitTester(c: SimDTMAESTop) extends PeekPokeTester(c) {

}

/**
 * Test class for AESTop
 */
class AESTopTester extends ChiselFlatSpec {

  val dut = "AESTop"
  val testDir = s"test_dir/$dut"
  val limit = 1000

  behavior of dut

  val defaultArgs = Array(
    "-tbn=treadle",
    "-tgvo=on"
  )

  it should "be able to encrypt AES-128 [AES-0001]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0001",
    () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {

      }
    } should be (true)
  }
}
