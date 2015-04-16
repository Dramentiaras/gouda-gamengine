#version 150 core

uniform sampler2D texture0_diffuse;
uniform sampler2D texture1_diffuse;
uniform bool ignoreAlpha;

in vec4 color;
in vec2 texCoord0;
in vec2 texCoord1;

out vec4 out_Color;

void main(void) {
	vec4 tex0Color = texture2D(texture0_diffuse, texCoord0);
	vec4 tex1Color = texture2D(texture1_diffuse, texCoord1);

	if (ignoreAlpha) {
	
		vec3 c = tex1Color.rgb * tex0Color.rgb * color.rgb;
		float a = tex0Color.a;
		
		out_Color = vec4(c, a);
	}
	else {
		
		out_Color = tex0Color * tex1Color * color;
	}
}