#version 120
uniform sampler2D sceneTex; // 0
uniform float rt_w;
uniform float rt_h;
uniform float vx_offset;

float offset[3] = float[]( 0.0, 1.3846153846, 3.2307692308 );
float weight[3] = float[]( 0.2270270270, 0.3162162162, 0.0702702703 );

void main() 
{ 
  vec3 tc = vec3(1.0, 0.0, 0.0);
  if (gl_TexCoord[0].x<(vx_offset-0.01))
  {
    tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgb * weight[0];
    for (int i=1; i<3; i++) 
    {
      tc += texture2D(sceneTex, gl_TexCoord[0].xy + vec2(0.0, offset[i])/rt_h).rgb * weight[i];
      tc += texture2D(sceneTex, gl_TexCoord[0].xy - vec2(0.0, offset[i])/rt_h).rgb * weight[i];
    }
  }
  else if (gl_TexCoord[0].x>=(vx_offset+0.01))
  {
    tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgb;
  }
	gl_FragColor = vec4(tc, 1.0);
}