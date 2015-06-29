static main(void)
{
auto fp, begin, end, dexbyte;
fp = fopen("C:\\dump.dex", "wb");
begin = r0;
end = r0 + r1;
for ( dexbyte = begin; dexbyte < end; dexbyte ++ )
fputc(Byte(dexbyte), fp);
}
