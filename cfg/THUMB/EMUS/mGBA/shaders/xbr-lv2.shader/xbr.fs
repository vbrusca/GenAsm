/*
   Hyllian's xBR-lv2 Shader
   
   Copyright (C) 2011-2015 Hyllian - sergiogdb@gmail.com

   Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal
   in the Software without restriction, including without limitation the rights
   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:

   The above copyright notice and this permission notice shall be included in
   all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
   THE SOFTWARE.


   Incorporates some of the ideas from SABR shader. Thanks to Joshua Street.
*/

uniform float XBR_Y_WEIGHT;
uniform float XBR_EQ_THRESHOLD;
uniform float XBR_SCALE;
uniform float XBR_LV2_COEFFICIENT;

const vec4 Ao = vec4( 1.0, -1.0, -1.0, 1.0 );
const vec4 Bo = vec4( 1.0,  1.0, -1.0,-1.0 );
const vec4 Co = vec4( 1.5,  0.5, -0.5, 0.5 );
const vec4 Ax = vec4( 1.0, -1.0, -1.0, 1.0 );
const vec4 Bx = vec4( 0.5,  2.0, -0.5,-2.0 );
const vec4 Cx = vec4( 1.0,  1.0, -0.5, 0.0 );
const vec4 Ay = vec4( 1.0, -1.0, -1.0, 1.0 );
const vec4 By = vec4( 2.0,  0.5, -2.0,-0.5 );
const vec4 Cy = vec4( 2.0,  0.0, -1.0, 0.5 );
const vec4 Ci = vec4(0.25, 0.25, 0.25, 0.25);

const vec3 Y = vec3(0.2126, 0.7152, 0.0722);

vec4 df(vec4 A, vec4 B)
{
	return vec4(abs(A-B));
}

float c_df(vec3 c1, vec3 c2) {
	vec3 df = abs(c1 - c2);
	return df.r + df.g + df.b;
}

bvec4 eq(vec4 A, vec4 B)
{
	return lessThan(df(A, B), vec4(XBR_EQ_THRESHOLD));
}

bvec4 and(bvec4 A, bvec4 B)
{
	return bvec4(A.x && B.x, A.y && B.y, A.z && B.z, A.w && B.w);
}

bvec4 nand(bvec4 A, bvec4 B)
{
	return bvec4(!(A.x && B.x), !(A.y && B.y), !(A.z && B.z), !(A.w && B.w));
}

bvec4 or(bvec4 A, bvec4 B)
{
	return bvec4(A.x || B.x, A.y || B.y, A.z || B.z, A.w || B.w);
}

vec4 weighted_distance(vec4 a, vec4 b, vec4 c, vec4 d, vec4 e, vec4 f, vec4 g, vec4 h)
{
	return (df(a,b) + df(a,c) + df(d,e) + df(d,f) + 4.0*df(g,h));
}

// GLSL shader autogenerated by cg2glsl.py.
#if __VERSION__ >= 130
#define varying in
#define COMPAT_TEXTURE texture
out vec4 FragColor;
#else
#define FragColor gl_FragColor
#define COMPAT_TEXTURE texture2D
#endif

#ifdef GL_ES
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
#define COMPAT_PRECISION mediump
#else
#define COMPAT_PRECISION
#endif
uniform sampler2D tex;
varying vec2 texCoord;
varying vec4 TEX1;
varying vec4 TEX2;
varying vec4 TEX3;
varying vec4 TEX4;
varying vec4 TEX5;
varying vec4 TEX6;
varying vec4 TEX7;

uniform vec2 texSize;

void main()
{
	bvec4 edri, edr, edr_left, edr_up; // px = pixel, edr = edge detection rule
	bvec4 interp_restriction_lv0, interp_restriction_lv1, interp_restriction_lv2_left, interp_restriction_lv2_up;
	vec4 fx, fx_left, fx_up, px; // inequations of straight lines.

	vec4 delta = vec4(1.0 / XBR_SCALE);
	vec4 deltaL = vec4(0.5/ XBR_SCALE, 1.0 / XBR_SCALE, 0.5 / XBR_SCALE, 1.0 / XBR_SCALE);
	vec4 deltaU = deltaL.yxwz;

    vec2 fp = fract(texCoord * texSize);

    vec3 A1 = COMPAT_TEXTURE(tex, TEX1.xw).rgb;
    vec3 B1 = COMPAT_TEXTURE(tex, TEX1.yw).rgb;
    vec3 C1 = COMPAT_TEXTURE(tex, TEX1.zw).rgb;

    vec3 A = COMPAT_TEXTURE(tex, TEX2.xw).rgb;
    vec3 B = COMPAT_TEXTURE(tex, TEX2.yw).rgb;
    vec3 C = COMPAT_TEXTURE(tex, TEX2.zw).rgb;

    vec3 D = COMPAT_TEXTURE(tex, TEX3.xw).rgb;
    vec3 E = COMPAT_TEXTURE(tex, TEX3.yw).rgb;
    vec3 F = COMPAT_TEXTURE(tex, TEX3.zw).rgb;

    vec3 G = COMPAT_TEXTURE(tex, TEX4.xw).rgb;
    vec3 H = COMPAT_TEXTURE(tex, TEX4.yw).rgb;
    vec3 I = COMPAT_TEXTURE(tex, TEX4.zw).rgb;

    vec3 G5 = COMPAT_TEXTURE(tex, TEX5.xw).rgb;
    vec3 H5 = COMPAT_TEXTURE(tex, TEX5.yw).rgb;
    vec3 I5 = COMPAT_TEXTURE(tex, TEX5.zw).rgb;

    vec3 A0 = COMPAT_TEXTURE(tex, TEX6.xy).rgb;
    vec3 D0 = COMPAT_TEXTURE(tex, TEX6.xz).rgb;
    vec3 G0 = COMPAT_TEXTURE(tex, TEX6.xw).rgb;

    vec3 C4 = COMPAT_TEXTURE(tex, TEX7.xy).rgb;
    vec3 F4 = COMPAT_TEXTURE(tex, TEX7.xz).rgb;
    vec3 I4 = COMPAT_TEXTURE(tex, TEX7.xw).rgb;

	vec4 b = transpose(mat4x3(B, D, H, F)) * (XBR_Y_WEIGHT * Y);
	vec4 c = transpose(mat4x3(C, A, G, I)) * (XBR_Y_WEIGHT * Y);
	vec4 e = transpose(mat4x3(E, E, E, E)) * (XBR_Y_WEIGHT * Y);
	vec4 d = b.yzwx;
	vec4 f = b.wxyz;
	vec4 g = c.zwxy;
	vec4 h = b.zwxy;
	vec4 i = c.wxyz;

	vec4 i4 = transpose(mat4x3(I4, C1, A0, G5)) * (XBR_Y_WEIGHT * Y);
	vec4 i5 = transpose(mat4x3(I5, C4, A1, G0)) * (XBR_Y_WEIGHT * Y);
	vec4 h5 = transpose(mat4x3(H5, F4, B1, D0)) * (XBR_Y_WEIGHT * Y);
	vec4 f4 = h5.yzwx;

	fx      = (Ao*fp.y+Bo*fp.x);
	fx_left = (Ax*fp.y+Bx*fp.x);
	fx_up   = (Ay*fp.y+By*fp.x);

	interp_restriction_lv1 = interp_restriction_lv0 = and(notEqual(e, f), notEqual(e, h));

    interp_restriction_lv2_left = and(notEqual(e, g), notEqual(d, g));
	interp_restriction_lv2_up   = and(notEqual(e, c), notEqual(b, c));

	vec4 fx45i = clamp((fx      + delta  - Co - Ci) / (2 * delta ), 0.0, 1.0);
	vec4 fx45  = clamp((fx      + delta  - Co     ) / (2 * delta ), 0.0, 1.0);
	vec4 fx30  = clamp((fx_left + deltaL - Co     ) / (2 * deltaL), 0.0, 1.0);
	vec4 fx60  = clamp((fx_up   + deltaU - Co     ) / (2 * deltaU), 0.0, 1.0);

	vec4 wd1 = weighted_distance( e, c, g, i, h5, f4, h, f);
	vec4 wd2 = weighted_distance( h, d, i5, f, i4, b, e, i);

	edri = and(lessThanEqual(wd1, wd2), interp_restriction_lv0);
	edr  = and(     lessThan(wd1, wd2), interp_restriction_lv1);

	edr_left = and(lessThanEqual((XBR_LV2_COEFFICIENT*df(f,g)), df(h,c)), interp_restriction_lv2_left);
	edr_up   = and(greaterThanEqual(df(f,g), (XBR_LV2_COEFFICIENT*df(h,c))), interp_restriction_lv2_up);

	edr = and(edr, nand(edri.yzwx, edri.wxyz));
	edr_left = and(and(edr_left, edr), eq(e, c));
	edr_up = and(and(edr_up, edr), eq(e, g));

	fx45  *= vec4(edr);
	fx30  *= vec4(edr_left);
	fx60  *= vec4(edr_up);
	fx45i *= vec4(edri);

    px = vec4(lessThanEqual(df(e, f), df(e, h)));

	vec4 maximos = max(max(fx30, fx60), max(fx45, fx45i));

	vec3 res1 = E;
	res1 = mix(res1, mix(H, F, px.x), maximos.x);
	res1 = mix(res1, mix(B, D, px.z), maximos.z);

	vec3 res2 = E;
	res2 = mix(res1, mix(F, B, px.y), maximos.y);
	res2 = mix(res1, mix(D, H, px.w), maximos.w);

    vec3 res = mix(res1, res2, step(c_df(E, res1), c_df(E, res2)));

    FragColor = vec4(res, 1.0);
} 
