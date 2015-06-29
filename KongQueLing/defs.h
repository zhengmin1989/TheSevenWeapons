/*

   This file contains definitions used by the Hex-Rays decompiler output.
   It has type definitions and convenience macros to make the
   output more readable.

   Copyright (c) 2007 Hex-Rays sprl

*/

#if defined(__GNUC__)
#define FMT_64 "ll"
typedef          long long ll;
typedef unsigned long long ull;
#elif defined(_MSC_VER)
typedef          __int64 ll;
typedef unsigned __int64 ull;
#define FMT_64 "I64"
#elif defined (__BORLANDC__)
typedef          __int64 ll;
typedef unsigned __int64 ull;
#define FMT_64 "L"
#else
#error "unknown compiler"
#endif
typedef unsigned int uint;
typedef unsigned char uchar;
typedef unsigned short ushort;
typedef unsigned long ulong;

// Partially defined types:
#define _BYTE  char
#define _WORD  short
#define _DWORD long
#define _QWORD ll
#define _LONGLONG __int128

#ifndef _WINDOWS_
typedef char BYTE;
typedef short WORD;
typedef long DWORD;
typedef long LONG;
#endif
typedef ll QWORD;
#ifndef __cplusplus
typedef int bool;       // we want to use bool in our C programs
#endif

// Some convenience macros to make partial accesses nicer
// first unsigned macros:
#define LOBYTE(x)   (*((_BYTE*)&(x)))   // low byte
#define LOWORD(x)   (*((_WORD*)&(x)))   // low word
#define LODWORD(x)  (*((_DWORD*)&(x)))  // low dword
#define HIBYTE(x)   (*((_BYTE*)&(x)+1))
#define HIWORD(x)   (*((_WORD*)&(x)+1))
#define HIDWORD(x)  (*((_DWORD*)&(x)+1))
#define BYTEn(x, n)   (*((_BYTE*)&(x)+n))
#define WORDn(x, n)   (*((_WORD*)&(x)+n))
#define BYTE1(x)   BYTEn(x,  1)         // byte 1 (counting from 0)
#define BYTE2(x)   BYTEn(x,  2)
#define BYTE3(x)   BYTEn(x,  3)
#define BYTE4(x)   BYTEn(x,  4)
#define BYTE5(x)   BYTEn(x,  5)
#define BYTE6(x)   BYTEn(x,  6)
#define BYTE7(x)   BYTEn(x,  7)
#define BYTE8(x)   BYTEn(x,  8)
#define BYTE9(x)   BYTEn(x,  9)
#define BYTE10(x)  BYTEn(x, 10)
#define BYTE11(x)  BYTEn(x, 11)
#define BYTE12(x)  BYTEn(x, 12)
#define BYTE13(x)  BYTEn(x, 13)
#define BYTE14(x)  BYTEn(x, 14)
#define BYTE15(x)  BYTEn(x, 15)
#define WORD1(x)   WORDn(x,  1)
#define WORD2(x)   WORDn(x,  2)         // third word of the object, unsigned
#define WORD3(x)   WORDn(x,  3)
#define WORD4(x)   WORDn(x,  4)
#define WORD5(x)   WORDn(x,  5)
#define WORD6(x)   WORDn(x,  6)
#define WORD7(x)   WORDn(x,  7)

// now signed macros (the same but with sign extension)
#define SLOBYTE(x)   (*((char*)&(x)))
#define SLOWORD(x)   (*((short*)&(x)))
#define SLODWORD(x)  (*((long*)&(x)))
#define SHIBYTE(x)   (*((char*)&(x)+1))
#define SHIWORD(x)   (*((short*)&(x)+1))
#define SHIDWORD(x)  (*((long*)&(x)+1))
#define SBYTEn(x, n)   (*((char*)&(x)+n))
#define SWORDn(x, n)   (*((short*)&(x)+n))
#define SBYTE1(x)   SBYTEn(x,  1)
#define SBYTE2(x)   SBYTEn(x,  2)
#define SBYTE3(x)   SBYTEn(x,  3)
#define SBYTE4(x)   SBYTEn(x,  4)
#define SBYTE5(x)   SBYTEn(x,  5)
#define SBYTE6(x)   SBYTEn(x,  6)
#define SBYTE7(x)   SBYTEn(x,  7)
#define SBYTE8(x)   SBYTEn(x,  8)
#define SBYTE9(x)   SBYTEn(x,  9)
#define SBYTE10(x)  SBYTEn(x, 10)
#define SBYTE11(x)  SBYTEn(x, 11)
#define SBYTE12(x)  SBYTEn(x, 12)
#define SBYTE13(x)  SBYTEn(x, 13)
#define SBYTE14(x)  SBYTEn(x, 14)
#define SBYTE15(x)  SBYTEn(x, 15)
#define SWORD1(x)   SWORDn(x,  1)
#define SWORD2(x)   SWORDn(x,  2)
#define SWORD3(x)   SWORDn(x,  3)
#define SWORD4(x)   SWORDn(x,  4)
#define SWORD5(x)   SWORDn(x,  5)
#define SWORD6(x)   SWORDn(x,  6)
#define SWORD7(x)   SWORDn(x,  7)

// Macros to represent some assembly instructions
// Feel free to modify them

#define __ROL__(x, y) __rotl__(x, y)       // Rotate left
#define __ROR__(x, y) __rotr__(x, y)       // Rotate right
#define __RCL__(x, y) invalid_operation    // Rotate left thru carry
#define __RCR__(x, y) invalid_operation    // Rotate right thru carry
#define __MKCADD__(x, y) invalid_operation // Generate carry flag for an addition
#define __MKOADD__(x, y) invalid_operation // Generate overflow flag for an addition
#define __MKCSHL__(x, y) invalid_operation // Generate carry flag for a shift left
#define __MKCSHR__(x, y) invalid_operation // Generate carry flag for a shift right
#define __MKCRCL__(x, y) invalid_operation // Generate carry flag for a RCL
#define __MKCRCR__(x, y) invalid_operation // Generate carry flag for a RCR
#define __SETO__(x, y)   invalid_operation // Generate overflow flags for (x-y)


// In the decompilation listing there are some objects declarared as _UNKNOWN
// because we could not determine their types. Since the C compiler does not
// accept void item declarations, we replace them by anything of our choice,
// for example a char:

#define _UNKNOWN char

#ifdef _MSC_VER
#define snprintf _snprintf
#define vsnprintf _vsnprintf
#endif
