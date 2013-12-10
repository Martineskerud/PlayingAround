uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;

attribute vec2 inTexCoord;
varying vec2 texCoord1;

void main(){
texCoord1 = inTexCoord;

gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}