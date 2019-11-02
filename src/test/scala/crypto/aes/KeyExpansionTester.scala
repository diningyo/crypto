// See LICENSE for license details.

package crypto.aes

import chisel3._
import chisel3.iotesters._

/**
 * Test class for KeyExpansion
 */
class KeyExpansionTester extends ChiselFlatSpec {

  val dut = "KeyExpansion"
  val testDir = s"test_run_dir/$dut"

  behavior of dut

  val defaultArgs = Array(
    s"-tn=$dut",
    "-tbn=treadle",
    "-tgvo=on"
  )

  it should s"be able to encrypt and decrypt FIP-197 C.1 AES-128 example [$dut-0001]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0001",
    () => new KeyExpansion) {
      c => new PeekPokeTester(c) {
        fail
      }
    } should be (true)
  }
}
