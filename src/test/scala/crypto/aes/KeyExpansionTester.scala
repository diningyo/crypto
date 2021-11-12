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

  /**
   * A.1 Expansion of a 128-bit Cipher Key
   *
   * Cipher Key = 2b 7e 15 16 28 ae d2 a6 ab f7 15 88 09 cf 4f 3c
   *
   * w0 = 2b7e1516 w1 = 28aed2a6 w2 = abf71588 w3 = 09cf4f3c
   *
   *  4 09cf4f3c cf4f3c09 8a84eb01 01000000 8b84eb01 2b7e1516 a0fafe17
   *  5 a0fafe17                                     28aed2a6 88542cb1
   *  6 88542cb1                                     abf71588 23a33939
   *  7 23a33939                                     09cf4f3c 2a6c7605
   *  8 2a6c7605 6c76052a 50386be5 02000000 52386be5 a0fafe17 f2c295f2
   *  9 f2c295f2                                     88542cb1 7a96b943
   * 10 7a96b943                                     23a33939 5935807a
   * 11 5935807a                                     2a6c7605 7359f67f
   * 12 7359f67f 59f67f73 cb42d28f 04000000 cf42d28f f2c295f2 3d80477d
   * 13 3d80477d                                     7a96b943 4716fe3e
   * 14 4716fe3e                                     5935807a 1e237e44
   * 15 1e237e44                                     7359f67f 6d7a883b
   * 16 6d7a883b 7a883b6d dac4e23c 08000000 d2c4e23c 3d80477d ef44a541
   * 17 ef44a541                                     4716fe3e a8525b7f
   * 18 a8525b7f                                     1e237e44 b671253b
   * 19 b671253b                                     6d7a883b db0bad00
   * 20 db0bad00 0bad00db 2b9563b9 10000000 3b9563b9 ef44a541 d4d1c6f8
   * 21 d4d1c6f8                                     a8525b7f 7c839d87
   * 22 7c839d87                                     b671253b caf2b8bc
   * 23 caf2b8bc                                     db0bad00 11f915bc
   * 24 11f915bc f915bc11 99596582 20000000 b9596582 d4d1c6f8 6d88a37a
   * 25 6d88a37a                                     7c839d87 110b3efd
   * 26 110b3efd                                     caf2b8bc dbf98641
   * 27 dbf98641                                     11f915bc ca0093fd
   * 28 ca0093fd 0093fdca 63dc5474 40000000 23dc5474 6d88a37a 4e54f70e
   * 29 4e54f70e                                     110b3efd 5f5fc9f3
   * 30 5f5fc9f3                                     dbf98641 84a64fb2
   * 31 84a64fb2                                     ca0093fd 4ea6dc4f
   * 32 4ea6dc4f a6dc4f4e 2486842f 80000000 a486842f 4e54f70e ead27321
   * 33 ead27321                                     5f5fc9f3 b58dbad2
   * 34 b58dbad2                                     84a64fb2 312bf560
   * 35 312bf560                                     4ea6dc4f 7f8d292f
   * 36 7f8d292f 8d292f7f 5da515d2 1b000000 46a515d2 ead27321 ac7766f3
   * 37 ac7766f3                                     b58dbad2 19fadc21
   * 38 19fadc21                                     312bf560 28d12941
   * 39 28d12941                                     7f8d292f 575c006e
   * 40 575c006e 5c006e57 4a639f5b 36000000 7c639f5b ac7766f3 d014f9a8
   * 41 d014f9a8                                     19fadc21 c9ee2589
   * 42 c9ee2589                                     28d12941 e13f0cc8
   * 43 e13f0cc8                                     575c006e b6630ca6
   */
  it should s"be able to expand 128-bit cipher key. (A.2 Expansion of a 128-bit Cipher Key) [$dut-0001]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0001",
    () => new KeyExpansion) {
      c => new PeekPokeTester(c) {
        val inKey = IndexedSeq(
          0x2b, 0x7e, 0x15, 0x16, 0x28, 0xae, 0xd2, 0xa6,
          0xab, 0xf7, 0x15, 0x88, 0x09, 0xcf, 0x4f, 0x3c
        ).map(BigInt(_)).reverse
        val expW = Seq(
          0x2b7e1516, 0x28aed2a6, 0xabf71588, 0x09cf4f3c, 0xa0fafe17, 0x88542cb1, 0x23a33939, 0x2a6c7605,
          0xf2c295f2, 0x7a96b943, 0x5935807a, 0x7359f67f, 0x3d80477d, 0x4716fe3e, 0x1e237e44, 0x6d7a883b,
          0xef44a541, 0xa8525b7f, 0xb671253b, 0xdb0bad00, 0xd4d1c6f8, 0x7c839d87, 0xcaf2b8bc, 0x11f915bc,
          0x6d88a37a, 0x110b3efd, 0xdbf98641, 0xca0093fd, 0x4e54f70e, 0x5f5fc9f3, 0x84a64fb2, 0x4ea6dc4f,
          0xead27321, 0xb58dbad2, 0x312bf560, 0x7f8d292f, 0xac7766f3, 0x19fadc21, 0x28d12941, 0x575c006e,
          0xd014f9a8, 0xc9ee2589, 0xe13f0cc8, 0xb6630ca6
        )
        poke(c.io.key_in, inKey)
        step(1)
        fail
      }
    } should be (true)
  }

  /**
   * A.2 Expansion of a 192-bit Cipher Key
   *
   * Cipher Key = 8e 73 b0 f7 da 0e 64 52 c8 10 f3 2b 80 90 79 e5 62 f8 ea d2 52 2c 6b 7b
   *
   * w0 = 8e73b0f7 w1 = da0e6452 w2 = c810f32b w3 = 809079e5 w4 = 62f8ead2 w5 = 522c6b7b
   *
   *  6 522c6b7b 2c6b7b52 717f2100 01000000 707f2100 8e73b0f7 fe0c91f7
   *  7 fe0c91f7                                     da0e6452 2402f5a5
   *  8 2402f5a5                                     c810f32b ec12068e
   *  9 ec12068e                                     809079e5 6c827f6b
   * 10 6c827f6b                                     62f8ead2 0e7a95b9
   * 11 0e7a95b9                                     522c6b7b 5c56fec2
   * 12 5c56fec2 56fec25c b1bb254a 02000000 b3bb254a fe0c91f7 4db7b4bd
   * 13 4db7b4bd                                     2402f5a5 69b54118
   * 14 69b54118                                     ec12068e 85a74796
   * 15 85a74796                                     6c827f6b e92538fd
   * 16 e92538fd                                     0e7a95b9 e75fad44
   * 17 e75fad44                                     5c56fec2 bb095386
   * 18 bb095386 095386bb 01ed44ea 04000000 05ed44ea 4db7b4bd 485af057
   * 19 485af057                                     69b54118 21efb14f
   * 20 21efb14f                                     85a74796 a448f6d9
   * 21 a448f6d9                                     e92538fd 4d6dce24
   * 22 4d6dce24                                     e75fad44 aa326360
   * 23 aa326360                                     bb095386 113b30e6
   * 24 113b30e6 3b30e611 e2048e82 08000000 ea048e82 485af057 a25e7ed5
   * 25 a25e7ed5                                     21efb14f 83b1cf9a
   * 26 83b1cf9a                                     a448f6d9 27f93943
   * 27 27f93943                                     4d6dce24 6a94f767
   * 28 6a94f767                                     aa326360 c0a69407
   * 29 c0a69407                                     113b30e6 d19da4e1
   * 30 d19da4e1 9da4e1d1 5e49f83e 10000000 4e49f83e a25e7ed5 ec1786eb
   * 31 ec1786eb                                     83b1cf9a 6fa64971
   * 32 6fa64971                                     27f93943 485f7032
   * 33 485f7032                                     6a94f767 22cb8755
   * 34 22cb8755                                     c0a69407 e26d1352
   * 35 e26d1352                                     d19da4e1 33f0b7b3
   * 36 33f0b7b3 f0b7b333 8ca96dc3 20000000 aca96dc3 ec1786eb 40beeb28
   * 37 40beeb28                                     6fa64971 2f18a259
   * 38 2f18a259                                     485f7032 6747d26b
   * 39 6747d26b                                     22cb8755 458c553e
   * 40 458c553e                                     e26d1352 a7e1466c
   * 41 a7e1466c                                     33f0b7b3 9411f1df
   * 42 9411f1df 11f1df94 82a19e22 40000000 c2a19e22 40beeb28 821f750a
   * 43 821f750a                                     2f18a259 ad07d753
   * 44 ad07d753                                     6747d26b ca400538
   * 45 ca400538                                     458c553e 8fcc5006
   * 46 8fcc5006                                     a7e1466c 282d166a
   * 47 282d166a                                     9411f1df bc3ce7b5
   * 48 bc3ce7b5 3ce7b5bc eb94d565 80000000 6b94d565 821f750a e98ba06f
   * 49 e98ba06f                                     ad07d753 448c773c
   * 50 448c773c                                     ca400538 8ecc7204
   * 51 8ecc7204                                     8fcc5006 01002202
   */
  it should s"be able to expand 192-bit cipher key. (A.2 Expansion of a 192-bit Cipher Key) [$dut-0002]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0002",
      () => new KeyExpansion) {
      c => new PeekPokeTester(c) {
        val inKey = IndexedSeq(
          0x8e, 0x73, 0xb0, 0xf7, 0xda, 0x0e, 0x64, 0x52,
          0xc8, 0x10, 0xf3, 0x2b, 0x80, 0x90, 0x79, 0xe5,
          0x62, 0xf8, 0xea, 0xd2, 0x52, 0x2c, 0x6b, 0x7b
        ).map(BigInt(_))
        val expW = Seq(
          0x8e73b0f7, 0xda0e6452, 0xc810f32b, 0x809079e5, 0x62f8ead2, 0x522c6b7b, 0xfe0c91f7, 0x2402f5a5,
          0xec12068e, 0x6c827f6b, 0x0e7a95b9, 0x5c56fec2, 0x4db7b4bd, 0x69b54118, 0x85a74796, 0xe92538fd,
          0xe75fad44, 0xbb095386, 0x485af057, 0x21efb14f, 0xa448f6d9, 0x4d6dce24, 0xaa326360, 0x113b30e6,
          0xa25e7ed5, 0x83b1cf9a, 0x27f93943, 0x6a94f767, 0xc0a69407, 0xd19da4e1, 0xec1786eb, 0x6fa64971,
          0x485f7032, 0x22cb8755, 0xe26d1352, 0x33f0b7b3, 0x40beeb28, 0x2f18a259, 0x6747d26b, 0x458c553e,
          0xa7e1466c, 0x9411f1df, 0x821f750a, 0xad07d753, 0xca400538, 0x8fcc5006, 0x282d166a, 0xbc3ce7b5,
          0xe98ba06f, 0x448c773c, 0x8ecc7204, 0x01002202
        )
        poke(c.io.key_in, inKey)
        step(1)
        fail
      }
    } should be (true)
  }

  /**
   * A.3 Expansion of a 256-bit Cipher Key
   *
   * Cipher Key = 60 3d eb 10 15 ca 71 be 2b 73 ae f0 85 7d 77 81 1f 35 2c 07 3b 61 08 d7 2d 98 10 a3 09 14 df f4
   *
   * w0 = 603deb10 w1 = 15ca71be w2 = 2b73aef0 w3 = 857d7781 w4 = 1f352c07 w5 = 3b6108d7 w6 = 2d9810a3 w7 = 0914dff4
   *
   *  8 0914dff4 14dff409 fa9ebf01 01000000 fb9ebf01 603deb10 9ba35411
   *  9 9ba35411                                     15ca71be 8e6925af
   * 10 8e6925af                                     2b73aef0 a51a8b5f
   * 11 a51a8b5f                                     857d7781 2067fcde
   * 12 2067fcde          b785b01d                   1f352c07 a8b09c1a
   * 13 a8b09c1a                                     3b6108d7 93d194cd
   * 14 93d194cd                                     2d9810a3 be49846e
   * 15 be49846e                                     0914dff4 b75d5b9a
   * 16 b75d5b9a 5d5b9ab7 4c39b8a9 02000000 4e39b8a9 9ba35411 d59aecb8
   * 17 d59aecb8                                     8e6925af 5bf3c917
   * 18 5bf3c917                                     a51a8b5f fee94248
   * 19 fee94248                                     2067fcde de8ebe96
   * 20 de8ebe96          1d19ae90                   a8b09c1a b5a9328a
   * 21 b5a9328a                                     93d194cd 2678a647
   * 22 2678a647                                     be49846e 98312229
   * 23 98312229                                     b75d5b9a 2f6c79b3
   * 24 2f6c79b3 6c79b32f 50b66d15 04000000 54b66d15 d59aecb8 812c81ad
   * 25 812c81ad                                     5bf3c917 dadf48ba
   * 26 dadf48ba                                     fee94248 24360af2
   * 27 24360af2                                     de8ebe96 fab8b464
   * 28 fab8b464          2d6c8d43                   b5a9328a 98c5bfc9
   * 29 98c5bfc9                                     2678a647 bebd198e
   * 30 bebd198e                                     98312229 268c3ba7
   * 31 268c3ba7                                     2f6c79b3 09e04214
   * 32 09e04214 e0421409 e12cfa01 08000000 e92cfa01 812c81ad 68007bac
   * 33 68007bac                                     dadf48ba b2df3316
   * 34 b2df3316                                     24360af2 96e939e4
   * 35 96e939e4                                     fab8b464 6c518d80
   * 36 6c518d80          50d15dcd                   98c5bfc9 c814e204
   * 37 c814e204                                     bebd198e 76a9fb8a
   * 38 76a9fb8a                                     268c3ba7 5025c02d
   * 39 5025c02d                                     09e04214 59c58239
   * 40 59c58239 c5823959 a61312cb 10000000 b61312cb 68007bac de136967
   * 41 de136967                                     b2df3316 6ccc5a71
   * 42 6ccc5a71                                     96e939e4 fa256395
   * 43 fa256395                                     6c518d80 9674ee15
   * 44 9674ee15          90922859                   c814e204 5886ca5d
   * 45 5886ca5d                                     76a9fb8a 2e2f31d7
   * 46 2e2f31d7                                     5025c02d 7e0af1fa
   * 47 7e0af1fa                                     59c58239 27cf73c3
   * 48 27cf73c3 cf73c327 8a8f2ecc 20000000 aa8f2ecc de136967 749c47ab
   * 49 749c47ab                                     6ccc5a71 18501dda
   * 50 18501dda                                     fa256395 e2757e4f
   * 51 e2757e4f                                     9674ee15 7401905a
   * 52 7401905a          927c60be                   5886ca5d cafaaae3
   * 53 cafaaae3                                     2e2f31d7 e4d59b34
   * 54 e4d59b34                                     7e0af1fa 9adf6ace
   * 55 9adf6ace                                     27cf73c3 bd10190d
   * 56 bd10190d 10190dbd cad4d77a 40000000 8ad4d77a 749c47ab fe4890d1
   * 57 fe4890d1                                     18501dda e6188d0b
   * 58 e6188d0b                                     e2757e4f 046df344
   * 59 046df344                                     7401905a 706c631e
   *
   */
  it should s"be able to expand 256-bit cipher key. (A.3 Expansion of a 256-bit Cipher Key) [$dut-0003]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0003",
      () => new KeyExpansion) {
      c => new PeekPokeTester(c) {
        val inKey = IndexedSeq(
          0x60, 0x3d, 0xeb, 0x10, 0x15, 0xca, 0x71, 0xbe,
          0x2b, 0x73, 0xae, 0xf0, 0x85, 0x7d, 0x77, 0x81,
          0x1f, 0x35, 0x2c, 0x07, 0x3b, 0x61, 0x08, 0xd7,
          0x2d, 0x98, 0x10, 0xa3, 0x09, 0x14, 0xdf, 0xf4
        ).map(BigInt(_))
        val expW = Seq(
          0x603deb10, 0x15ca71be, 0x2b73aef0, 0x857d7781, 0x1f352c07, 0x3b6108d7, 0x2d9810a3, 0x0914dff4,
          0x9ba35411, 0x8e6925af, 0xa51a8b5f, 0x2067fcde, 0xa8b09c1a, 0x93d194cd, 0xbe49846e, 0xb75d5b9a,
          0xd59aecb8, 0x5bf3c917, 0xfee94248, 0xde8ebe96, 0xb5a9328a, 0x2678a647, 0x98312229, 0x2f6c79b3,
          0x812c81ad, 0xdadf48ba, 0x24360af2, 0xfab8b464, 0x98c5bfc9, 0xbebd198e, 0x268c3ba7, 0x09e04214,
          0x68007bac, 0xb2df3316, 0x96e939e4, 0x6c518d80, 0xc814e204, 0x76a9fb8a, 0x5025c02d, 0x59c58239,
          0xde136967, 0x6ccc5a71, 0xfa256395, 0x9674ee15, 0x5886ca5d, 0x2e2f31d7, 0x7e0af1fa, 0x27cf73c3,
          0x749c47ab, 0x18501dda, 0xe2757e4f, 0x7401905a, 0xcafaaae3, 0xe4d59b34, 0x9adf6ace, 0xbd10190d,
          0xfe4890d1, 0xe6188d0b, 0x046df344, 0x706c631e
        )
        poke(c.io.key_in, inKey)
        step(1)
        fail
      }
    } should be (true)
  }
}
